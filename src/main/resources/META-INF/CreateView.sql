create VIEW user_role_view AS select
user.USERNAME AS `username`,
user.PASSWORD AS `password`,
role.ROLEDESC AS `roledesc`
from ((user_roles join user
on((user_roles.User_userid = user.ID)))
join role
on((user_roles.Role_roleid = role.ID)));