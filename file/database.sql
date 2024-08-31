drop database if exists api_platform;

create database if not exists api_platform;

use api_platform;

drop table if exists user, interface_info, user_interface_info;

create table user
(
    id            bigint      not null comment '主键',
    user_name     varchar(8)  not null comment '用户昵称',
    user_avatar   varchar(256) comment '用户头像地址',
    user_account  varchar(16) not null comment '账号',
    user_password char(32)    not null comment '密码',
    user_role     varchar(10) not null comment '权限角色',
    access_key    varchar(32) not null comment '公共密钥',
    secret_key    varchar(32) not null comment '私有密钥',
    create_time   datetime comment '创建时间'                             default current_timestamp,
    update_time   datetime comment '更新时间' on update current_timestamp default current_timestamp,
    is_delete     char(1) comment '是否删除'                              default '0',
    primary key (id)
) comment '用户表';

create table if not exists interface_info
(
    id              bigint        not null comment '主键',
    name            varchar(32)   not null comment '接口名称',
    url             varchar(256)  not null comment 'URL',
    description     varchar(128)  not null comment '接口描述',
    method          varchar(8)    not null comment '请求方法',
    request_param   varchar(512)  not null comment '请求参数',
    request_header  varchar(512)  not null comment '请求头',
    request_body    varchar(1024) not null                                  default '' comment '请求体',
    response_header varchar(512)  not null comment '响应头',
    response_body   varchar(1024) not null                                  default '' comment '响应体',
    status          char(1)       not null comment '接口状态'               default '0',
    create_time     datetime comment '创建时间'                             default current_timestamp,
    update_time     datetime comment '更新时间' on update current_timestamp default current_timestamp,
    is_delete       char(1) comment '是否删除'                              default '0',
    primary key (id)
) comment '接口信息表';

create table if not exists user_interface_info
(
    id                bigint       not null comment '主键',
    interface_info_id bigint       not null comment '接口ID',
    user_id           bigint       not null comment '用户ID',
    total_num         int unsigned not null comment '总量',
    left_num          int unsigned not null comment '剩余量',
    create_time       datetime comment '创建时间'                             default current_timestamp,
    update_time       datetime comment '更新时间' on update current_timestamp default current_timestamp,
    is_delete         char(1) comment '是否删除'                              default '0',
    primary key (id)
) comment '用户接口表';





create index user_account_password_index on user(user_account, user_password);

create index user_access_secret_index on user (access_key, secret_key);

create index user_interface_info_id_left_index on user_interface_info(interface_info_id, left_num);

create index user_interface_info_user_info_index on user_interface_info(interface_info_id, user_id);





insert into user(id, user_name, user_avatar, user_account, user_password, user_role, access_key, secret_key)
values (1,
        '1',
        'https://tse2-mm.cn.bing.net/th/id/OIP-C.8YIMPN_JsVthJAmknMvHQAHaDk?rs=1&pid=ImgDetMain',
        '1',
        'c4ca4238a0b923820dcc509a6f75849b',
        'admin',
        '1',
        '1');

insert into user(id, user_name, user_avatar, user_account, user_password, user_role, access_key, secret_key)
values (2,
        '1',
        'https://tse2-mm.cn.bing.net/th/id/OIP-C.8YIMPN_JsVthJAmknMvHQAHaDk?rs=1&pid=ImgDetMain',
        '2',
        'c4ca4238a0b923820dcc509a6f75849b',
        'user',
        '1',
        '1');


SELECT id,
       user_name,
       user_avatar,
       user_account,
       user_password,
       user_role,
       access_key,
       secret_key,
       create_time,
       update_time,
       is_delete
FROM user
WHERE is_delete = '0'
  AND (user_account = '1' AND user_password = 'c4ca4238a0b923820dcc509a6f75849b');



select id,
       name,
       url,
       description,
       method,
       request_param,
       response_header,
       request_header,
       status,
       create_time,
       update_time,
       is_delete,
       user_id,
       total_num
from interface_info ii,
     (select interface_info_id, user_id, total_num
      from user_interface_info uii
      order by uii.total_num - uii.left_num
      limit 0, 3) inner_table
where ii.id = inner_table.interface_info_id;



CREATE USER if not exists canal IDENTIFIED BY 'canal';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
FLUSH PRIVILEGES;


show variables like 'log_bin';