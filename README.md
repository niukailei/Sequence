
#sequence-service使用配置说明

sequence-service支持redis多实例和redis集群两种模式,通过配置文件指定redis模式,以.yml配置文件为例:

* cluster: true //true为集群模式,false为多实例模式
* redis:  
      longTypeCon: 192.168.40.10:6000,192.168.40.10:6001,192.168.40.10:6002 //longType类型 redis地址  
      dateTypeCon: 192.168.40.10:6000,192.168.40.10:6001,192.168.40.10:6002 //dateType类型 redis地址  
      password: 666 //reids密码
    

#调用举例
* localhost:9000/generateIdBatch?batchSize=1000
* localhost:9000/generateDateStrId?keyName=ccc&keySize=5