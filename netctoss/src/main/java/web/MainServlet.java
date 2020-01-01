package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

public class MainServlet extends HttpServlet {

	@Override
	protected void service(
		HttpServletRequest req, 
		HttpServletResponse res)
		throws ServletException, IOException {
		//判断请求路径
		String path = req.getServletPath();
		if("/findCost.do".equals(path)) {
			//查询资费
			findCost(req, res);
		} else if("/toAddCost.do".equals(path)) {
			//打开增加资费页
			toAddCost(req, res);
		} else if("/addCost.do".equals(path)) {
			//增加保存资费
			addCost(req, res);
		} else if("/toUpdateCost.do".equals(path)) {
			//打开修改资费页
			toUpdateCost(req, res);
		} else if("/updateCost.do".equals(path)) {
			//修改保存资费
			updateCost(req, res);
		} else if("/deleteCost.do".equals(path)) {
			deleteCost(req,res);
		} else if("/toLogin.do".equals(path)) {
			//打开登录页
			toLogin(req, res);
		} else if("/toIndex.do".equals(path)) {
			//打开主页
			toIndex(req, res);
		} else if("/login.do".equals(path)) {
			//登录校验
			login(req, res);
		} else if("/createImg.do".equals(path)) {
			//生成验证码
			createImg(req, res);
		} else {
			//错误路径
			throw new RuntimeException("查无此页");
		}
	}

	protected void createImg(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException {
		//创建验证码及图片
		Object[] objs = ImageUtil.createImage();
		//将验证码存入session
		HttpSession sn = req.getSession();
		sn.setAttribute("imgCode", objs[0]);
		//将图片输出给浏览器
		res.setContentType("image/png");
		OutputStream os = res.getOutputStream();
		ImageIO.write(
			(BufferedImage) objs[1], "png", os);
		os.close();
	}
	
	protected void login(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException {
		//接收请求参数
		String adminCode = 
			req.getParameter("adminCode");
		String pwd = 
			req.getParameter("password");
		String code = 
			req.getParameter("code");
		//校验验证码
		HttpSession sn = req.getSession();
		String imgCode = (String)
			sn.getAttribute("imgCode");
		if(code == null
			|| !code.equalsIgnoreCase(imgCode)) {
			//验证码错误
			req.setAttribute(
				"error", "验证码错误");
			req.getRequestDispatcher(
				"WEB-INF/main/login.jsp")
				.forward(req, res);
			return;
		}
		
		//登录验证
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode(adminCode);
		if(a == null) {
			//账号不存在，转发回登录页
			//目标:/netctoss/WEB-INF/main/login.jsp
			req.setAttribute(
				"error", "账号不存在");
			req.getRequestDispatcher(
				"WEB-INF/main/login.jsp")
				.forward(req, res);
		} else if(!a.getPassword().equals(pwd)) {
			//密码错误，转发回登录页
			//目标:/netctoss/WEB-INF/main/login.jsp
			req.setAttribute(
				"error", "密码错误");
			req.getRequestDispatcher(
				"WEB-INF/main/login.jsp")
				.forward(req, res);
		} else {
			//将账号记录到Cookie中，
			//便于其他功能页面上显示账号。
			Cookie ck = new Cookie(
				"adminCode",adminCode);
			res.addCookie(ck);
			//将账号存入session，便于
			//后续做登录检查的业务。
			sn.setAttribute(
				"adminCode", adminCode);
			//成功，重定向到主页
			//当前:/netctoss/login.do
			//目标:/netctoss/toIndex.do
			res.sendRedirect("toIndex.do");
		}
	}
	
	protected void toIndex(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException {
		//当前:/netctoss/toIndex.do
		//目标:/netctoss/WEB-INF/main/index.jsp
		req.getRequestDispatcher(
			"WEB-INF/main/index.jsp")
			.forward(req, res);
	}
	
	protected void toLogin(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException {
		//当前:/netctoss/toLogin.do
		//目标:/netctoss/WEB-INF/main/login.jsp
		req.getRequestDispatcher(
			"WEB-INF/main/login.jsp")
			.forward(req, res);
	}
	
	protected void deleteCost(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException{
		String id = req.getParameter("id");
		CostDao dao = new CostDao();
		dao.delete(new Integer(id));
		res.sendRedirect("findCost.do");
	}
	
	protected void updateCost(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException{
		req.setCharacterEncoding("utf-8");
		//获取请求参数
		//参数的名字可以任意命名，
		//为了便于记忆通常和实体属性同名。
		String costId = 
			req.getParameter("costId");
		String name = 
			req.getParameter("name");
		String baseDuration = 
			req.getParameter("baseDuration");
		String baseCost =
			req.getParameter("baseCost");
		String unitCost = 
			req.getParameter("unitCost");
		String descr = 
			req.getParameter("descr");
		String costType = 
			req.getParameter("costType");
		//修改这些数据
		Cost c = new Cost();
		c.setCostId(new Integer(costId));
		c.setName(name);
		c.setDescr(descr);
		c.setCostType(costType);
		if(baseDuration != null
			&& !baseDuration.equals("")) {
			c.setBaseDuration(
				new Integer(baseDuration));
		}
		if(baseCost != null
			&& !baseCost.equals("")) {
			c.setBaseCost(
				new Double(baseCost));
		}
		if(unitCost != null
			&& !unitCost.equals("")) {
			c.setUnitCost(
				new Double(unitCost));
		}
		CostDao dao = new CostDao();
		dao.update(c);
		//重定向到查询
		res.sendRedirect("findCost.do");
	}
	
	protected void toUpdateCost(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException {
		//接收请求参数
		String id = req.getParameter("id");
		//查询该资费数据
		CostDao dao = new CostDao();
		Cost cost = 
			dao.findById(new Integer(id));
		//转发到修改页面
		//当前:/netctoss/toUpdateCost.do
		//目标:/netctoss/WEB-INF/cost/cost_update.jsp
		req.setAttribute("cost", cost);
		req.getRequestDispatcher(
			"WEB-INF/cost/update.jsp")
			.forward(req, res);
	}
	
	protected void addCost(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws IOException {
		req.setCharacterEncoding("utf-8");
		//接收请求数据
		String name = req.getParameter("name");
		String baseDuration = 
			req.getParameter("baseDuration");
		String baseCost = 
			req.getParameter("baseCost");
		String unitCost = 
			req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		String costType= 
			req.getParameter("costType");
		//保存数据
		Cost c = new Cost();
		c.setName(name);
		if(baseDuration != null
			&& !baseDuration.equals("")) {
			c.setBaseDuration(
				new Integer(baseDuration));
		}
		if(baseCost != null
			&& !baseCost.equals("")) {
			c.setBaseCost(
				new Double(baseCost));
		}
		if(unitCost != null
			&& !unitCost.equals("")) {
			c.setUnitCost(
				new Double(unitCost));
		}
		c.setDescr(descr);
		c.setCostType(costType);
		CostDao dao = new CostDao();
		dao.save(c);
		//重定向到查询资费
		//当前：/netctoss/addCost.do
		//目标：/netctoss/findCost.do
		res.sendRedirect("findCost.do");
	}
	
	protected void toAddCost(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException {
		//转发到打开资费页面
		//当前：/netctoss/toAddCost.do
		//目标：/netctoss/WEB-INF/cost/cost_add.jsp
		req.getRequestDispatcher(
			"WEB-INF/cost/add.jsp")
			.forward(req, res);
	}
	
	protected void findCost(
		HttpServletRequest req,
		HttpServletResponse res) 
		throws ServletException, IOException {
		//获取请求参数
		String page = req.getParameter("page");
		if(page == null || page.equals("")) {
			page = "1";
		}
		//获取常量
		String size = 
			getServletContext()
				.getInitParameter("size");
		//查询资费
		CostDao dao = new CostDao();
		List<Cost> list = 
			dao.findByPage(
				new Integer(page), 
				new Integer(size));
		//查询总行数，计算出总页数
		int rows = dao.findRows();
		int total = rows/new Integer(size);
		if(rows%new Integer(size) != 0) {
			total++;
		}
		//转发到查询页面
		req.setAttribute("costs", list);
		req.setAttribute("total", total);
		req.setAttribute("page", page);
		//当前：/netctoss/findCost.do
		//目标：/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher(
			"WEB-INF/cost/find.jsp")
			.forward(req, res);
	}
	
}










