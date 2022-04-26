CREATE TABLE IF NOT EXISTS "user" (
    id varchar(32) primary key not null,
    created_at timestamp null,
    updated_at timestamp null,
    deleted smallint null,

    username varchar(255) null default null,
    password varchar(255) null default null,
    enable smallint null default 1
);

COMMENT ON TABLE "user" IS '文件信息表';
COMMENT ON COLUMN "user".id IS '主键';
COMMENT ON COLUMN "user".created_at IS '创建时间';
COMMENT ON COLUMN "user".updated_at IS '最后更新时间';
COMMENT ON COLUMN "user".deleted IS '软删除字段；1 - 已删除，0 - 未删除';

COMMENT ON COLUMN "user".username IS '登录账号，各种奇奇怪怪的账号';
COMMENT ON COLUMN "user".password IS '密码';
COMMENT ON COLUMN "user".enable IS '是否被禁用：0 - 禁用，1 - 正常';