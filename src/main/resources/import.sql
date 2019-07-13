INSERT INTO test_user (username,password) values ('lmh','202cb962ac59075b964b07152d234b70');
INSERT INTO user_roles (username,role_name) values ('lmh','admin');
INSERT INTO permissions (permission,role_name) values ('user:add,user:delete,user:update,user:select,user:updateRole','admin');