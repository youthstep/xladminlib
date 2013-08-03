xl.VCode = {
	init : function(vcode, vimg, vbtn) {
		var vc = vcode, vi = vimg, vb = vbtn;
		if (vbtn)
			$(vbtn).click(refresh);
		vi.click(refresh);
		function refresh() {
			vi.show();
			vb.show();
			vi.attr("src", xl.cfg.root + "libfun/vcode?" + new Date().getTime());
		}
		return {
			refresh : refresh,
			getKey : function() {
				return $.cookie("VERIFY_KEY");
			}
		};
	}
};

xl.loginForm = {
	init : function() {
		var vcode = xl.VCode.init($("#vcode"), $("#vcodeImg"), $("#vcodeBtn"));
		vcode.refresh();
		$("#username").bind('keydown', keydown);
		$("#flpwd").focus(function() {
			$("#flpwdLab").hide();
		}).bind('keydown', keydown);
		$("#vcode").focus(function() {
			$("#vcodeLab").hide();
		}).bind('keydown', keydown);
		$("#flLoginBtn").click(
				function() {
					if (bl.isEmpty(bl("username").value)) {
						showError("请输入用户名");
					} else if (bl.isEmpty(bl("flpwd").value)) {
						showError("请输入密码");
					} else if (bl.isEmpty(bl("vcode").value)) {
						showError("请输入验证码");
					} else {
						commonService.login(bl("username").value, $.md5(bl("flpwd").value), bl("vcode").value,
								function(rtn) {
									switch(rtn.loginStatus){
										case xl.login.OK : 
											xl.toMainPage();
											break;
										case xl.login.USERNAME_ERR : 
											showError("用户名错误");
											vcode.refresh();
											break;
										case xl.login.IP_ERR : 
											showError("IP错误");
											vcode.refresh();
											break;
										case xl.login.NOT_INUSE :
											showError("用户失效");
											vcode.refresh();
											break;
										case xl.login.EXPIRE :
											showError("用户过期");
											vcode.refresh();
											break;
										case xl.login.VERIFY_CODE_ERR :
											showError("验证码错误");
											vcode.refresh();
											break;
										case xl.login.PASSWORD_ERR :
											showError("密码错误");
											vcode.refresh();
											break;
										default :
											showError("未知错误#" + rtn.loginStatus);
											vcode.refresh();
											break;
									}
								});
					}
				});
		function keydown(event) {
			if (event.keyCode == "13") {
				$("#flLoginBtn").click();
				return false;
			}
		}
		function showLogin() {
			vcode.refresh();
			if (cfg.initLoginout)
				cfg.initLoginout();
		}
		function hideLogin() {
			if (cfg.initLogin)
				cfg.initLogin();
		}
		function showError(msg) {
			$("#loginErrLi").show();
			$("#loginErr").show().text(msg);
		}
	}
};
$(function() {
	$('#username').focus();
	xl.libfun.loadService([ "commonService" ], function() {
		xl.loginForm.init();
		// init pic
		var picRoot = "images/";
		var x = parseInt(Math.random() * (xl.cfg.loginPicNum - 1)) + 1;
		if (x < 10)
			x = '0' + x;
		document.getElementById('pic').src = picRoot + x + ".jpg";
	});
});