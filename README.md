
# 万合优选（源自商品甄选） 小结 
### 2023.7.10 -- 2023.11.20
项目源自B站尚硅谷-尚品甄选，采用B2C模式，使用SpringBoot+SpringCloud微服务架构，采用前后端分离开发模式。项目包含两个系统：后台管理系统 和 前台用户系统（手机端）

## 实现功能

| 后台功能 | 前台功能 |
|--|--|
| 用户登录 | 用户注册和登录 |
| 用户管理 | 首页数据显示 |
| 角色管理 | 所有分类显示 |
| 菜单管理 | 商品列表和详情 |
| 商品管理 | 购物车 |
| 订单管理 | 订单功能 |
  |  |


## 实现技术

| 表的关系 | 后端技术 |  前端技术 |
|--|--| --|
| 权限相关表 | SpringBoot + SpringCloud |  Element-Admin |
| 商品相关表 | Redis + dicker + aliyun短信 |  ES6 |
| 订单相关表 | EasyExcel + miniIo |  NPM |
| 多表的优化设计 | IOC + AOP +  |  Axios |


### 心得
尚品甄选项目开始于23.7.11，历时90多天，连敲带看，期间报错无数，我个人是根据给的需求自己先写，后面再根据课件给的代码改，在写的时候也发现自己有很多不足，尤其是订单和结算业务，需要考虑的方面不是一般的多，个人感觉技术还是次要的，如何写好一个需求才是重中之重。
