CREATE TABLE IF NOT EXISTS sys_file_info (
    id varchar(32) primary key not null,
    created_at timestamp default current_timestamp null,
    updated_at timestamp default current_timestamp null,
    deleted smallint null default 0,

    filename        varchar(255) null default null,
    file_size       bigint       null default null,
    mime_type       varchar(255) null default null,
    relative_path   varchar(255) null default null
);

COMMENT ON TABLE sys_file_info IS '文件信息表';
COMMENT ON COLUMN sys_file_info.id IS '主键';
COMMENT ON COLUMN sys_file_info.created_at IS '创建时间';
COMMENT ON COLUMN sys_file_info.updated_at IS '最后更新时间';
COMMENT ON COLUMN sys_file_info.deleted IS '软删除字段；1 - 已删除，0 - 未删除';

COMMENT ON COLUMN sys_file_info.filename IS '文件名称';
COMMENT ON COLUMN sys_file_info.file_size IS '文件大小';
COMMENT ON COLUMN sys_file_info.mime_type IS '文件类型';
COMMENT ON COLUMN sys_file_info.relative_path IS '相对路径';
