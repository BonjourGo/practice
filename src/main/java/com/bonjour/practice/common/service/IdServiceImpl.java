package com.bonjour.practice.common.service;

import cn.hutool.core.net.NetUtil;
import com.bonjour.practice.common.entity.WorkNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @authur tc
 * @date 2024/6/17 9:33
 */
@Service
public class IdServiceImpl implements IdService {

    /**
     * 理论上当前服务的IP地址获取一次即可。
     */
//    private static final String IP_ADDRESS = IpAddressUtil.findAddress();
    private String serverPort;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdServiceImpl.class);
    private String bit = "2";

    /**
     * 16:st
     * 1525278457256 定长时间  相减
     * 1834516102567 最大时间
     * 18:et
     * 8796093022208  定长时间  相加
     * 35184372088831 最大时间
     */
    private static long startTime = 1420041600000L;
    private static final long TWEPOCH_ET = 8796093022208L;
    private static final long TWEPOCH_ST = -1525278457256L;
    private static final String TWEPOCH_ET_KEY = "et";
    private static final String TWEPOCH_ST_KEY = "st";

    long workerIdBits = 5L;
    long sequenceBits = 10L;

    private long twepoch = TWEPOCH_ET;
    private long maxWorkerId = ~(-1L << 5);
    private long workerIdShift = sequenceBits;
    private long timestampLeftShift = sequenceBits + workerIdBits;
    private long sequenceMask = ~(-1L << sequenceBits);
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private static final SecureRandom RANDOM = new SecureRandom();

    Map<String, Long> isMap = new HashMap<>();

//    public IdServiceImpl(String serverPort) {
//        this.bit = "et";
//        long workerIdBits = 5L;
//        long sequenceBits = 12L;
//        //最大能够分配的workerid =31
//        this.maxWorkerId = ~(-1L << workerIdBits);
//        this.workerIdShift = sequenceBits;
//        this.sequenceMask = ~(-1L << sequenceBits);
//        this.timestampLeftShift = sequenceBits + workerIdBits;
//        this.serverPort = serverPort;
//        if (TWEPOCH_ET_KEY.equals(bit)) {
//            twepoch = TWEPOCH_ET;
//        } else if (TWEPOCH_ST_KEY.equals(bit)) {
//            twepoch = TWEPOCH_ST;
//
//        }
//    }

    @Override
    public synchronized Long get(String key) {
        long id = -1L;
        long workerId;
        checkTwepoch();
        workerId = getWorkId();
        if (workerId > maxWorkerId) {
            //实例化Timer类
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isMap.clear();
                    delNodes();
                    this.cancel();
                }
            }, 1);
        }
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            long refusedSeconds = (lastTimestamp - timestamp) / 1000;
            throw new RuntimeException("Clock moved backwards. Refusing for %d seconds");

        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //seq 为0的时候表示是下一毫秒时间开始对seq做随机
//                sequence = RANDOM.nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果是新的ms开始
            sequence = 0L;
//            sequence = RANDOM.nextInt(100);
        }
        lastTimestamp = timestamp;
//        LOGGER.info("timestamp={}, twepoch={}, timestampLeftShift={}, workerId={}, workerIdShift={}, sequence={}", timestamp, twepoch, timestampLeftShift, workerId, workerIdShift, sequence);
        if (bit != null) {
            id = ((timestamp - startTime) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
        }
        LOGGER.info("id = {}, timestamp = {}, sequence = {}", id, timestamp, sequence);
        return id;

    }

    protected void checkTwepoch() {
        if (twepoch == -1) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        // ((timestamp + twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
        // timestamp=1718605777296, twepoch=8796093022208, timestampLeftShift=17, workerId=5, workerIdShift=12, sequence=5
        System.out.println(238418579102L / 1000);
        System.out.println((238418579102L / 1000) / 60);
        long timestamp = 1718605777296L;
                      // 238418579102
        long twepoch = 8796093022208L;
        // 1288834974657L 1525278457256L
        long startTime = 1420041600000L;
        long timestampLeftShift = 17L;
        long workerId = 5L;
        long workerIdShift = 12L;
        long sequence = 100L;
        Date date = new Date();
        date.setTime(8796093022208L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//        System.out.println(simpleDateFormat.par(date));
        System.out.println((2018605777296L + 8796093022208L) << 14);
//        System.out.println(5L << 12);
        System.out.println(((timestamp - 1525278457256L) << 13) | (workerId << workerIdShift) | sequence);
        System.out.println(1378182601048608768L | 5);
        String ip = "";
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
            System.out.println(NetUtil.getLocalMacAddress());
            System.out.println(ip);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected long getWorkId() {
        long workerId;
        String ip = "";
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String port = "this.serverPort";
        if (isMap.containsKey(ip + port)) {
            workerId = isMap.get(ip + port);
        } else {
            WorkNode workNode = queryWorkNode(ip);
            if (workNode == null) {
                // insert new record
                workNode = new WorkNode();
                workNode.setIp(ip);
                workNode = insertWorkNode(workNode);
            }
            workerId = workNode.getId();
            LOGGER.info("put map id {} ip {}", workNode.getId(), workNode.getIp());
            isMap.put(ip + port, workNode.getId());
        }

//        WorkNode workerNode = buildWorkerNode(ip, port);
//        WorkNode WorkNode;
//        if (isMap.containsKey(ip + port)) {
//            workerId = isMap.get(ip + port);
//        } else {
//            WorkNode = this.getWorkerNodeByHostPort(workerNode);
//            if (WorkNode != null) {
//                workerId = WorkNode.getId();
//                isMap.put(WorkNode.getIp(), workerId);
//                LOGGER.info("START SUCCESS USE WORKERID-{}", workerId);
//            } else {
//                workerId = assignWorkerId(ip, port);
//                LOGGER.info("START SUCCESS USE WORKERID-{}", workerId);
//            }
//        }
        return workerId;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public long assignWorkerId(String ip, String port) {
        WorkNode WorkNode = buildWorkerNode(ip, port);
        addWorkerNode(WorkNode);
        LOGGER.info("Add worker node:{}", WorkNode);
        return WorkNode.getId();
    }

    private WorkNode buildWorkerNode(String ip, String port) {
        WorkNode WorkNode = new WorkNode();
        long maxid = this.getMaxWorkerId();
        WorkNode.setIp(ip);
        WorkNode.setId(maxid + 1);
        return WorkNode;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public WorkNode getWorkerNodeByHostPort(WorkNode WorkNode) {
        List<WorkNode> list = jdbcTemplate.query("select * from WORK_NODE where ip = ?",
                getRowMapper(),
                WorkNode.getId());
        return list.isEmpty() ? null : list.get(0);
    }

    public WorkNode queryWorkNode(String ip) {
        List<WorkNode> list = jdbcTemplate.query("select * from WORK_NODE where ip = ?",
                getRowMapper(),
                ip);
        return list.isEmpty() ? null : list.get(0);
    }

    public void addWorkerNode(WorkNode WorkNode) {
        jdbcTemplate.update("insert into WORK_NODE(ID,IP)values(?,?)",
                WorkNode.getId(),
                WorkNode.getIp());
    }

    public WorkNode insertWorkNode(WorkNode workNode) {
        jdbcTemplate.update("insert into WORK_NODE(ID,IP)select (SELECT ifnull(max(id), 0) + 1 from work_node), ?",
                workNode.getIp());
        workNode = queryWorkNode(workNode.getIp());
        return workNode;
    }

    public int getMaxWorkerId() {
        Integer integer = jdbcTemplate.queryForObject("SELECT count(*) as id FROM WORK_NODE", Integer.class);
        System.out.println(integer);
        return integer;
//        return Objects.requireNonNull(jdbcTemplate.queryForObject("SELECT count(*) as id FROM WORK_NODE", Integer.class));
    }

    public void delNodes() {
        jdbcTemplate.update("DELETE FROM WORK_NODE");
    }

    private RowMapper<WorkNode> getRowMapper() {
        return (rs, rowNum) -> {
            WorkNode entity = new WorkNode();
            entity.setId(rs.getLong("ID"));
            entity.setIp(rs.getString("IP"));
            return entity;
        };
    }
}
