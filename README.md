sftp2db project
===================================================




项目说明
-----------
> 主要用于读取远程服务器中的指定文件夹中csv文件数据并存储到数据库中


文件说明
-----------
* com.adidas.sftp.config.BeanConfig: 数据库连接配置
* com.adidas.sftp.util.Download: sftp服务器信息连接配置
* com.adidas.sftp.service.impl.SftpServiceImpl: 读写实现

使用步骤说明
-----------
1. 下载程序
1. 补充 config/application.yml中sftp信息和db信息
1. 根据实际项目需求调整com.adidas.sftp.service.impl.SftpServiceImpl中的文件读写逻辑
1. 发版程序
1. 调用示例

``
curl --location --request GET 'http://localhost:8080/auth-data-init/init?remotePath=/member/infos'
``