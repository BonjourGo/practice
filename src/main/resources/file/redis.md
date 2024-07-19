## redis

### 基本数据类型 String、Hash、List、Set、Zset

### 其他数据类型bitmap、geohash、hyperloglog、streams



#### String 字符串

字符串底层有三种存储的方式，字符串类型通过redis自己的简单字符串sds实现

int  存储值能转换为整数时使用

raw 存储的是字符串且长度大于44字节

embstr 存储值不能转换整数且长度小于等于44字节

常用命令 

```
> set name code  # 设置，成功返回 ok
> get name       # 获取，不存在返回 nil
> mset name1 code1 name2 code2 # 批量设置
> mget name1 name2 # 批量获取
> exists name    # 是否存在，1:存在；0:不存在
> del name       # 删除，1:成功
> expire name 5  # 设置过期时间
> setex name 5 code # 等价于 set + expire
> setnx name code   # 如果name不存在，则创建，存在，则创建失败返回 0
> incr name # 对name++
> incrby name 5 # 对name + n
> setnx key 
```

#### Hash 哈希

hash底层是有ziplist和hashtable实现，新版本ziplist已经由**listpack** 实现

元素小于512使用ziplist 否则 hashtable

适合存储对象结构，可单独获取/更改对象中某个字段

常用命令

```
# 存储一个哈希表key的键值
HSET key field value   
# 获取哈希表key对应的field键值
HGET key field

# 在一个哈希表key中存储多个键值对
HMSET key field value [field value...] 
# 批量获取哈希表key中多个field键值
HMGET key field [field ...]       
# 删除哈希表key中的field键值
HDEL key field [field ...]    

# 返回哈希表key中field的数量
HLEN key       
# 返回哈希表key中所有的键值
HGETALL key 

# 为哈希表key中field键的值加上增量n
HINCRBY key field n
```



#### List 列表

底层是ziplist（压缩列表）和linkedlist（双向链表）实现 3.2后使用quicklist

可用作简单的消息队列，先进先出

常用命令

```
> lpush key value # 向左添加一个元素
> rpop key # 从右侧获取list列表第一个元素

# 将一个或多个值value插入到key列表的表头(最左边)，最后的值在最前面
> LPUSH key value [value ...] 

# 将一个或多个值value插入到key列表的表尾(最右边)
> RPUSH key value [value ...]

# 移除并返回key列表的头元素
> LPOP key     

# 移除并返回key列表的尾元素
> RPOP key 

# 返回列表key中指定区间内的元素，区间以偏移量start和stop指定，从0开始
> LRANGE key start stop

# 从key列表表头弹出一个元素，没有就阻塞timeout秒，如果timeout=0则一直阻塞
> BLPOP key [key ...] timeout

# 从key列表表尾弹出一个元素，没有就阻塞timeout秒，如果timeout=0则一直阻塞
> BRPOP key [key ...] timeout
```



#### Set 集合

不可重复且无序

底层是由整数集合或哈希表组成

集合元素可转化为整数且小于512个时使用整数集合，否则使用哈希表

可用点赞、共同关注计算、抽奖等

常用命令

```
# 往集合key中存入元素，元素存在则忽略，若key不存在则新建
> SADD key member [member ...]

# 从集合key中删除元素
> SREM key member [member ...] 

# 获取集合key中所有元素
> SMEMBERS key

# 获取集合key中的元素个数
> SCARD key

# 判断member元素是否存在于集合key中
> SISMEMBER key member

# 从集合key中随机选出count个元素，元素不从key中删除
> SRANDMEMBER key [count]

# 从集合key中随机选出count个元素，元素从key中删除 可用作随机抽奖
> SPOP key [count]
```



#### Zset 有序集合

不可重复但有序

底层实现：压缩列表或跳表实现，最新压缩列表已经被quickList取代

当元素小于128且大小小于64位使用压缩列表，否则跳表

使用场景 排行榜

常用命令

```
# 往有序集合key中加入带分值元素
> ZADD key score member [[score member]...]   

# 往有序集合key中删除元素
> ZREM key member [member...]   

# 返回有序集合key中元素member的分值
> ZSCORE key member

# 返回有序集合key中元素个数
> ZCARD key 

# 为有序集合key中元素member的分值加上increment
> ZINCRBY key increment member 

# 正序获取有序集合key从start下标到stop下标的元素
> ZRANGE key start stop [WITHSCORES]

# 倒序获取有序集合key从start下标到stop下标的元素
> ZREVRANGE key start stop [WITHSCORES]

# 返回有序集合中指定分数区间内的成员，分数由低到高排序。
> ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]

# 返回指定成员区间内的成员，按字典正序排列, 分数必须相同。
> ZRANGEBYLEX key min max [LIMIT offset count]

# 返回指定成员区间内的成员，按字典倒序排列, 分数必须相同
> ZREVRANGEBYLEX key max min [LIMIT offset count]
```

