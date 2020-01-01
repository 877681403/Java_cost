<%@page pageEncoding="utf-8"%>
<!-- 由于本页面可以被其他任意页面复用，
	因此无法确定访问路径的来源，
	使用绝对路径来设置超链接的地址。 -->
<%-- 
request提供了获取项目名的方法：
  request.getContextPath()
如何通过EL获取该值：
  ${pwd} -> request.getAttribute("pwd")
  ${user.name} -> 
    request.getAttribute("user").getName()
  ${request.contextPath} ->
     request.getAttribute("request").getContextPath()
        这是错误的逻辑；
EL中可以直接获取pageContext对象：
  1.该对象是9个内置对象之一；
  2.该对象引用了其他8个对象，包括request；
  3.在EL中可以直接获取该对象，再通过它
       获取到request，然后把request当做一个
       普通对象看待，则可以访问其任何get方法。
  4.举例：
    ${pageContext} -> 返回pageContext
    ${pageContext.request} -> 
      pageContext.getRequest()
    ${pageContext.request.contextPath} ->
      pageContext.getRequest().getContextPath()
--%>
<ul id="menu">
    <li><a href="${pageContext.request.contextPath}/toIndex.do" class="index_off"></a></li>
    <li><a href="role/role_list.html" class="role_off"></a></li>
    <li><a href="admin/admin_list.html" class="admin_off"></a></li>
    <li><a href="${pageContext.request.contextPath}/findCost.do" class="fee_off"></a></li>
    <li><a href="account/account_list.html" class="account_off"></a></li>
    <li><a href="service/service_list.html" class="service_off"></a></li>
    <li><a href="bill/bill_list.html" class="bill_off"></a></li>
    <li><a href="report/report_list.html" class="report_off"></a></li>
    <li><a href="user/user_info.html" class="information_off"></a></li>
    <li><a href="user/user_modi_pwd.html" class="password_off"></a></li>
</ul>


