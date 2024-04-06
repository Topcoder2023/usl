
# H-ui.admin.Pro v1.0

<center><img src="https://images.h-ui.net/www/logo.png" width="200"></center>

#### 项目介绍
H-ui.admin.Pro版是一款由国人开发的轻量级扁平化网站后台模板，适合中小型CMS后台系统。

#### 功能介绍

1. 响应式布局（大小屏适配）
2. 左侧三级菜单
3. 集成百度Echarts 4.0图表插件
4. 集成H-ui.iconfont 图标字库
5. 集成百度编辑器UEditor
6. 集成表单验证、日期插件


#### 目录机构

    ├── lib                     - 第三方插件、库文件目录
    │   ├──echarts              - 百度编辑器
    │   ├──Hui-iconfont         - 图标字库
    │   ├──jquery               - jquery
    │   ├──jquery.contextmenu   - 右键菜单插件
    │   ├──jquery.SuperSlide    - 幻灯片插件
    │   ├──jquery.validation    - 表单验证插件，满足各种表单
    │   ├──layer                - 弹窗插件
    │   ├──laypage              - 翻页插件
    │   ├──lightbox2            - 照片墙插件
    │   ├──nprogress            - 进度条插件
    │   ├──ueitor               - 百度编辑器
    │   ├──webuploader          - 百度文件上传插件
    │   ├──zTree                - 树插件
    │
    ├── static                  - static目录存系统UI相关的资源文件
    │   ├── h-ui                 - h-ui前端框架库（不建议改动文件）
    │       ├── css                 - css文件
    │       ├── js                  - js文件
    │       ├── images              - image文件
    │   ├── h-ui.admin.pro       - h-ui.admin.pro资源（不建议改动，除非需要改界面皮肤或风格）
    │       ├── css                 - css文件
    │       ├── js                  - js文件
    │       ├── images              - image文件
    │   ├── business             - 业务相关资源文件（放客户自己，请尽量将业务有关的写到这里面，方便维护）
    │       ├── css                 - css文件
    │       ├── js                  - js文件
    │       ├── images              - image文件
    │
    ├── temp                   - demo相关的临时图片，可以自由删除
    ├── _blank.html                   - 空白页模板
    ├── _footer.html                  - 公共页脚片段
    ├── _header.html                  - 公共头部片段
    ├── _menu.html                    - 左侧菜单片段
    ├── _meta.html                    - head区meta代码片段
    ├── login.html                    - 后台登录页模板
    ├── index.html                    - 首页（欢迎页）
    ├── admin-add.html                - 添加管理员页
    ├── admin-list.html               - 管理员列表页
    ├── admin-permission.html         - 权限管理页
    ├── admin-role-add.html           - 角色添加页
    ├── admin-role.html               - 角色管理页
    ├── article-add.html              - 资讯添加（编辑）页
    ├── article-list.html             - 资讯管理页
    ├── change-password.html          - 修改密码页
    ├── charts-1.html                 - 统计图表之折线图页
    ├── charts-2.html                 - 统计图表之区域图页
    ├── charts-3.html                 - 统计图表之柱状图页
    ├── charts-4.html                 - 统计图表之饼状图页
    ├── charts-5.html                 - 统计图表之散点图页
    ├── comment-list.html             - 评论管理（列表）页
    ├── feedback-list.html            - 意见反馈页
    ├── member-add.html               - 会员添加页
    ├── member-lsit.html              - 会员列表页
    ├── member-del.html               - 删除的会员列表页
    ├── member-racord-browse.html     - 浏览记录页
    ├── member-racord-download.html   - 下载记录页
    ├── member-racord-share.html      - 分享记录页
    ├── member-show.html              - 会员详情显示页
    ├── picture-add.html              - 图片添加页
    ├── picture-list.html             - 图片管理（列表）页
    ├── picture-show.html             - 图片详情页
    ├── product-add.html              - 产品添加页
    ├── product-list.html             - 产品管理（列表）页
    ├── product-del.html              - 删除的产品页
    ├── product-brand.html            - 品牌管理页
    ├── product-category-add.html     - 产品分类添加页
    ├── product-category.html         - 产品分类页
    ├── system-base.html              - 系统设置页
    ├── system-category.html          - 栏目管理页
    ├── system-data.html              - 数据字典页
    ├── system-log.html               - 系统日志页
    ├── system-shielding.html         - 屏蔽词页
    ├── error-404.html                - 异常页面-404
    ├── error-500.html                - 异常页面-500
    ├── README.md                     - 开发必读

#### 软件界面

<img src="https://images.h-ui.net/www/Hui-admin-pro-1.png" width="800">

<img src="https://images.h-ui.net/www/Hui-admin-pro-2.png" width="375">

#### 布局结构

```html
<!DOCTYPE HTML>
<html>
  <head>
    此处放 一大堆meta信息，
    外部css样式
    标题
    关键词
    巴拉巴拉……
    总之是标准的固定模式，很重要，一般不用轻易修改。
  </head>

  <body>
    <aside class="Hui-admin-aside-wrapper">
    	左侧导航菜单模块，宽250px
    </aside>
    <div class="Hui-admin-aside-mask"></div>
    <!--不要删这个div，pc端没用，小屏幕（移动端）是导航菜单的遮罩层-->

    <div class="Hui-admin-dislpayArrow">
    	点击箭头，隐藏显示左侧菜单
    </div>

    <section class="Hui-admin-article-wrapper">
    	整个右侧最外层包裹

    	<header class="Hui-navbar">
    		顶部管理员登录信息和菜单,高64px
    	</header>

    	<div class="Hui-admin-article">
    		整个右侧内容区外层
    		<nav class="breadcrumb" style="background-color:#fff;padding: 0 24px">
    			面包屑导航
    		</nav>

    		<article class="Hui-admin-content clearfix">
    		  整个右侧内容区
    		</article>

    		<footer class="footer Hui-admin-footer">
    	     页脚
    		</footer>
    	</div>
    </section>

    全站脚本通常放到底部，后加载，有助于页面有限展示，提升性能。
  </body>
</html>
```
#### 更新日志

v1.0.6

- 增加一级菜单
- 优化登录页增加表单验证

v1.0.5

- 优化日期控件

v1.0.4

- 增加顶部导航菜单

v1.0.2

- 增加皮肤，datatables.js

#### 版权声明

- H-ui.admin.Pro 是一个付费版本，版权归北京颖杰联创科技有限公司所有。不提供demo预览和免费下载入口，收费49元。
- 付费用户可商用，可开发成自己的系统。
- 出现以下几种情况，请购买者慎重，明确自己需求目的，已经购买，表示您已接受以下声明，无法退款。
	- H-ui.admin.Pro 只是个前端模板，不带后台功能和数据库，不是完整的后台管理系统，他的诞生只是为后端开发工程师提供一套相对好看一点的后台页面，简少后端工程师拼页面的过程。
	- H-ui.admin.Pro 只是根据常用的业务场景做出一些页面，不代表最终用户的业务，不一定能满足最终用户的业务需求，请用户根据自己的切身业务扩展自己的页面。
