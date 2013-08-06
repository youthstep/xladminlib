//jquery plugin
(function($){
	var rotateLeft = function(lValue, iShiftBits) {
		return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
	};
	
	var addUnsigned = function(lX, lY) {
		var lX4, lY4, lX8, lY8, lResult;
		lX8 = (lX & 0x80000000);
		lY8 = (lY & 0x80000000);
		lX4 = (lX & 0x40000000);
		lY4 = (lY & 0x40000000);
		lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
		if (lX4 & lY4) return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
		if (lX4 | lY4) {
			if (lResult & 0x40000000) return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
			else return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
		} else {
			return (lResult ^ lX8 ^ lY8);
		}
	};
	
	var F = function(x, y, z) {
		return (x & y) | ((~ x) & z);
	}
	
	var G = function(x, y, z) {
		return (x & z) | (y & (~ z));
	}
	
	var H = function(x, y, z) {
		return (x ^ y ^ z);
	}
	
	var I = function(x, y, z) {
		return (y ^ (x | (~ z)));
	}
	
	var FF = function(a, b, c, d, x, s, ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(F(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};
	
	var GG = function(a, b, c, d, x, s, ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(G(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};
	
	var HH = function(a, b, c, d, x, s, ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(H(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};
	
	var II = function(a, b, c, d, x, s, ac) {
		a = addUnsigned(a, addUnsigned(addUnsigned(I(b, c, d), x), ac));
		return addUnsigned(rotateLeft(a, s), b);
	};
	
	var convertToWordArray = function(string) {
		var lWordCount;
		var lMessageLength = string.length;
		var lNumberOfWordsTempOne = lMessageLength + 8;
		var lNumberOfWordsTempTwo = (lNumberOfWordsTempOne - (lNumberOfWordsTempOne % 64)) / 64;
		var lNumberOfWords = (lNumberOfWordsTempTwo + 1) * 16;
		var lWordArray = Array(lNumberOfWords - 1);
		var lBytePosition = 0;
		var lByteCount = 0;
		while (lByteCount < lMessageLength) {
			lWordCount = (lByteCount - (lByteCount % 4)) / 4;
			lBytePosition = (lByteCount % 4) * 8;
			lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
			lByteCount++;
		}
		lWordCount = (lByteCount - (lByteCount % 4)) / 4;
		lBytePosition = (lByteCount % 4) * 8;
		lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
		lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
		lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
		return lWordArray;
	};
	
	var wordToHex = function(lValue) {
		var WordToHexValue = "", WordToHexValueTemp = "", lByte, lCount;
		for (lCount = 0; lCount <= 3; lCount++) {
			lByte = (lValue >>> (lCount * 8)) & 255;
			WordToHexValueTemp = "0" + lByte.toString(16);
			WordToHexValue = WordToHexValue + WordToHexValueTemp.substr(WordToHexValueTemp.length - 2, 2);
		}
		return WordToHexValue;
	};
	
	var uTF8Encode = function(string) {
		string = string.replace(/\x0d\x0a/g, "\x0a");
		var output = "";
		for (var n = 0; n < string.length; n++) {
			var c = string.charCodeAt(n);
			if (c < 128) {
				output += String.fromCharCode(c);
			} else if ((c > 127) && (c < 2048)) {
				output += String.fromCharCode((c >> 6) | 192);
				output += String.fromCharCode((c & 63) | 128);
			} else {
				output += String.fromCharCode((c >> 12) | 224);
				output += String.fromCharCode(((c >> 6) & 63) | 128);
				output += String.fromCharCode((c & 63) | 128);
			}
		}
		return output;
	};
	
	$.extend({
		md5: function(string) {
			var x = Array();
			var k, AA, BB, CC, DD, a, b, c, d;
			var S11=7, S12=12, S13=17, S14=22;
			var S21=5, S22=9 , S23=14, S24=20;
			var S31=4, S32=11, S33=16, S34=23;
			var S41=6, S42=10, S43=15, S44=21;
			string = uTF8Encode(string);
			x = convertToWordArray(string);
			a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
			for (k = 0; k < x.length; k += 16) {
				AA = a; BB = b; CC = c; DD = d;
				a = FF(a, b, c, d, x[k+0],  S11, 0xD76AA478);
				d = FF(d, a, b, c, x[k+1],  S12, 0xE8C7B756);
				c = FF(c, d, a, b, x[k+2],  S13, 0x242070DB);
				b = FF(b, c, d, a, x[k+3],  S14, 0xC1BDCEEE);
				a = FF(a, b, c, d, x[k+4],  S11, 0xF57C0FAF);
				d = FF(d, a, b, c, x[k+5],  S12, 0x4787C62A);
				c = FF(c, d, a, b, x[k+6],  S13, 0xA8304613);
				b = FF(b, c, d, a, x[k+7],  S14, 0xFD469501);
				a = FF(a, b, c, d, x[k+8],  S11, 0x698098D8);
				d = FF(d, a, b, c, x[k+9],  S12, 0x8B44F7AF);
				c = FF(c, d, a, b, x[k+10], S13, 0xFFFF5BB1);
				b = FF(b, c, d, a, x[k+11], S14, 0x895CD7BE);
				a = FF(a, b, c, d, x[k+12], S11, 0x6B901122);
				d = FF(d, a, b, c, x[k+13], S12, 0xFD987193);
				c = FF(c, d, a, b, x[k+14], S13, 0xA679438E);
				b = FF(b, c, d, a, x[k+15], S14, 0x49B40821);
				a = GG(a, b, c, d, x[k+1],  S21, 0xF61E2562);
				d = GG(d, a, b, c, x[k+6],  S22, 0xC040B340);
				c = GG(c, d, a, b, x[k+11], S23, 0x265E5A51);
				b = GG(b, c, d, a, x[k+0],  S24, 0xE9B6C7AA);
				a = GG(a, b, c, d, x[k+5],  S21, 0xD62F105D);
				d = GG(d, a, b, c, x[k+10], S22, 0x2441453);
				c = GG(c, d, a, b, x[k+15], S23, 0xD8A1E681);
				b = GG(b, c, d, a, x[k+4],  S24, 0xE7D3FBC8);
				a = GG(a, b, c, d, x[k+9],  S21, 0x21E1CDE6);
				d = GG(d, a, b, c, x[k+14], S22, 0xC33707D6);
				c = GG(c, d, a, b, x[k+3],  S23, 0xF4D50D87);
				b = GG(b, c, d, a, x[k+8],  S24, 0x455A14ED);
				a = GG(a, b, c, d, x[k+13], S21, 0xA9E3E905);
				d = GG(d, a, b, c, x[k+2],  S22, 0xFCEFA3F8);
				c = GG(c, d, a, b, x[k+7],  S23, 0x676F02D9);
				b = GG(b, c, d, a, x[k+12], S24, 0x8D2A4C8A);
				a = HH(a, b, c, d, x[k+5],  S31, 0xFFFA3942);
				d = HH(d, a, b, c, x[k+8],  S32, 0x8771F681);
				c = HH(c, d, a, b, x[k+11], S33, 0x6D9D6122);
				b = HH(b, c, d, a, x[k+14], S34, 0xFDE5380C);
				a = HH(a, b, c, d, x[k+1],  S31, 0xA4BEEA44);
				d = HH(d, a, b, c, x[k+4],  S32, 0x4BDECFA9);
				c = HH(c, d, a, b, x[k+7],  S33, 0xF6BB4B60);
				b = HH(b, c, d, a, x[k+10], S34, 0xBEBFBC70);
				a = HH(a, b, c, d, x[k+13], S31, 0x289B7EC6);
				d = HH(d, a, b, c, x[k+0],  S32, 0xEAA127FA);
				c = HH(c, d, a, b, x[k+3],  S33, 0xD4EF3085);
				b = HH(b, c, d, a, x[k+6],  S34, 0x4881D05);
				a = HH(a, b, c, d, x[k+9],  S31, 0xD9D4D039);
				d = HH(d, a, b, c, x[k+12], S32, 0xE6DB99E5);
				c = HH(c, d, a, b, x[k+15], S33, 0x1FA27CF8);
				b = HH(b, c, d, a, x[k+2],  S34, 0xC4AC5665);
				a = II(a, b, c, d, x[k+0],  S41, 0xF4292244);
				d = II(d, a, b, c, x[k+7],  S42, 0x432AFF97);
				c = II(c, d, a, b, x[k+14], S43, 0xAB9423A7);
				b = II(b, c, d, a, x[k+5],  S44, 0xFC93A039);
				a = II(a, b, c, d, x[k+12], S41, 0x655B59C3);
				d = II(d, a, b, c, x[k+3],  S42, 0x8F0CCC92);
				c = II(c, d, a, b, x[k+10], S43, 0xFFEFF47D);
				b = II(b, c, d, a, x[k+1],  S44, 0x85845DD1);
				a = II(a, b, c, d, x[k+8],  S41, 0x6FA87E4F);
				d = II(d, a, b, c, x[k+15], S42, 0xFE2CE6E0);
				c = II(c, d, a, b, x[k+6],  S43, 0xA3014314);
				b = II(b, c, d, a, x[k+13], S44, 0x4E0811A1);
				a = II(a, b, c, d, x[k+4],  S41, 0xF7537E82);
				d = II(d, a, b, c, x[k+11], S42, 0xBD3AF235);
				c = II(c, d, a, b, x[k+2],  S43, 0x2AD7D2BB);
				b = II(b, c, d, a, x[k+9],  S44, 0xEB86D391);
				a = addUnsigned(a, AA);
				b = addUnsigned(b, BB);
				c = addUnsigned(c, CC);
				d = addUnsigned(d, DD);
			}
			var tempValue = wordToHex(a) + wordToHex(b) + wordToHex(c) + wordToHex(d);
			return tempValue.toLowerCase();
		}
	});
})(jQuery);

(function ($, document, undefined) {

	var pluses = /\+/g;

	function raw(s) {
		return s;
	}

	function decoded(s) {
		return decodeURIComponent(s.replace(pluses, ' '));
	}

	var config = $.cookie = function (key, value, options) {

		// write
		if (value !== undefined) {
			options = $.extend({}, config.defaults, options);

			if (value === null) {
				options.expires = -1;
			}

			if (typeof options.expires === 'number') {
				var days = options.expires, t = options.expires = new Date();
				t.setDate(t.getDate() + days);
			}

			value = config.json ? JSON.stringify(value) : String(value);

			return (document.cookie = [
				encodeURIComponent(key), '=', config.raw ? value : encodeURIComponent(value),
				options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
				options.path    ? '; path=' + options.path : '',
				options.domain  ? '; domain=' + options.domain : '',
				options.secure  ? '; secure' : ''
			].join(''));
		}

		// read
		var decode = config.raw ? raw : decoded;
		var cookies = document.cookie.split('; ');
		for (var i = 0, l = cookies.length; i < l; i++) {
			var parts = cookies[i].split('=');
			if (decode(parts.shift()) === key) {
				var cookie = decode(parts.join('='));
				return config.json ? JSON.parse(cookie) : cookie;
			}
		}

		return null;
	};

	config.defaults = {};

	$.removeCookie = function (key, options) {
		if ($.cookie(key) !== null) {
			$.cookie(key, null, options);
			return true;
		}
		return false;
	};

})(jQuery, document);

/** bl **/
/**
 * 同document.getElementByID()
 * @namespace 常用工具
 * @param {String} e page element id
 * @return page element
 */
var bl = function(e){
	return document.getElementById(e);
};
bl.V = function(e){
	return document.getElementById(e).value;
};
/**
 * 返回当前URL上的变量 "http://xxx/?name1=value1&name2=value2&...."
 * @param {String} n 变量名
 * @return 变量值
 */
bl.P = function(n){
	var hrefstr, pos, parastr, para, tempstr;
	hrefstr = window.location.href;
	if(hrefstr.indexOf("#")>-1)hrefstr=hrefstr.substring(0,hrefstr.indexOf("#"));
	pos = hrefstr.indexOf("?");
	parastr = hrefstr.substring(pos + 1);
	para = parastr.split("&");
	tempstr = "";
	for (var i = 0; i < para.length; i++) {
		tempstr = para[i];
		pos = tempstr.indexOf("=");
		if (tempstr.substring(0, pos).toLowerCase() == n.toLowerCase()) {
			return tempstr.substring(pos + 1);
		}
	}
	return null;
};
bl.isEmpty = function(s){
	return s&&$.trim(s)!=''?false:true;
};
bl.delCookie = function(name) {
	var expireDate=new Date(new Date().getTime());
	document.cookie = name + "= ; path=/; domain=kankan.com; expires=" + expireDate.toGMTString();
};

bl.reg = {
	url:/^http[s]{0,1}:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=^,#]*)?$/i,
	bankid:/^[0-9]{5,20}$/
};

/*** xunlei libfun**/
var xl = {
	log : function(s){
		console.log(s);
	},
	toMainPage : function(){
		window.location.href = xl.cfg.root + "static/main.html";
	},
	toLoginPage : function(){
		window.location.href = xl.cfg.root + "static/index.html";
	}
};

xl.cfg = {
	root:"http://local.xunlei.com:8080/admin/",
	page:"static/",
	svr:"http://local.xunlei.com:8080/admin/dwr/",
	ver:"4.0",
	loginPicNum:23
};

xl.login = {
	DEFAULT : -1,
	OK : 0,
	ERR : 1,
	USERNAME_ERR : 3,
	IP_ERR : 4,
	PASSWORD_ERR : 5,
	NOT_INUSE : 6,
	EXPIRE : 7,
	VERIFY_CODE_ERR : 8
};

xl.libfun = {};

xl.libfun.Alert = {
	//content,title,p{}
	show : function(c,t,p){
		alert(c);
	}
};

/**
 * 初始化dwr services
 * @param sarr service数组，名称如“commonService”
 * @param over 全部加载完后的回调
 */
xl.libfun.loadService = function(sarr,over){
	var n = -1;
	$.getScript(xl.cfg.svr + "engine.js").done(function(){
		n++;
		if(n === sarr.length){
			over();
		}else{
			loadOne(sarr, n);
		}
	});
	
	function loadOne(sarr, n){
		$.getScript(xl.cfg.svr + "interface/" + sarr[n] + ".js").done(function(){
			n++;
			if(n === sarr.length){
				over();
			}else{
				loadOne(sarr, n);
			}
		});
	}
};

xl.libfun.QueryInfo = function(p){
	var pageNo = p && p.pageNo ? p.pageNo : 1
		, pageSize = p && p.pageSize ? p.pageSize : 20
		, sortColumn = p && p.sortColumn ? p.sortCoumn : ""
		, sqlCondition = p && p.sqlCondition ? p.sqlCondition : "";
	
	return {
		//dwr 不支持 getPageNo直接转换成属性
		toObj : function(){
			return {pageNo : pageNo, pageSize : pageSize, sortColumn : sortColumn, sqlCondition : sqlCondition};
		},
		getPageNo : function(){
			return pageNo;
		},
		setPageNo : function(p){
			pageNo = p;
		},
		getPageSize : function(){
			return pageSize;
		},
		setPageSize : function(p){
			pageSize = p;
		},
		getSortColumn : function(){
			return sortColumn;
		},
		setSortColumn : function(p){
			sortColumn = p;
		},
		getSqlCondition : function(){
			return sqlCondition;
		},
		setSqlCondition : function(p){
			sqlCondition = p;
		}
	};
};

//p{grid:,ser:,datas:,autoQuery:true,queryInfo:,beforeAdd:,afterAdd,beforeUpdate:,}
xl.libfun.QueryContext = function(p){
	var grid = p.grid, ser = p.ser, datas = [], queryInfo = p.queryInfo, autoQuery = $.type(p.autoQuery) === "boolean" ? p.autoQuery : true;
	
	function query(){
		ser.query({}, queryInfo.toObj(),function(rtn){
			grid.jqGrid("clearGridData");
			datas = rtn.datas;
			grid.jqGrid("setGridParam", {datatype : "local", page : queryInfo.getPageNo(), rowTotal : rtn.rowcount, data : rtn.datas});
			grid.trigger("reloadGrid");
		});
	}
	
	function getJqGridDataProxy(){
		return function(opt, type){
			switch(opt.data.oper){
			case "add" : 
				if($.isFunction(p.beforeAdd)){
					p.beforeAdd({data : opt.data, rowId : opt.data.id});
				}
				ser.insert(opt.data, function(rtn){
					if(rtn.code === 0){
						opt.complete({status : 200}, "");
						if(autoQuery){
							query();
						}
						if($.isFunction(p.afterAdd)){
							p.afterAdd({data : opt.data, rowId : opt.data.id});
						}
					}else{
						opt.complete({status : 500, statusText : "rtn.code : " + rtn.code}, "rtn.code error");
					}
				});
				break;
			case "edit" :
				opt.data["seqid"] = datas[opt.data.id - 1].seqid;
				if($.isFunction(p.beforeUpdate)){
					p.beforeUpdate({data : opt.data, rowId : opt.data.id});
				}
				ser.update(opt.data, function(rtn){
					if(rtn.code === 0){
						opt.complete({status : 200}, "");
						if(autoQuery){
							query();
						}
					}else{
						opt.complete({status : 500, statusText : "rtn.code : " + rtn.code}, "rtn.code error");
					}
				});
				break;
			case "del" :
				var i = 0, ids = opt.data.id.split(","), delObjs = [];
				for(i = 0; i < ids.length; i++){
					delObjs.push(datas[ids[i] - 1]);
				}
				ser.deleteSome(delObjs, function(rtn){
					if(rtn.code === 0){
						opt.complete({status : 200}, "");
						if(autoQuery){
							query();
						}
					}else{
						opt.complete({status : 500, statusText : "rtn.code : " + rtn.code}, "rtn.code error");
					}
				});
				break;
			default :
				throw new Error("unknow oper : " + opt.data.oper);
				break;
			}
		};
	}
	
	return {
		query : query,
		getJqGridDataProxy : getJqGridDataProxy,
		getQueryInfo : function(){
			return queryInfo;
		},
		getDatas : function(){
			return datas;
		},
		getSer : function(){
			return ser;
		}
	};
};

//{grid:,context:,}
xl.libfun.jqGridInit = function(p){
	var grid = $(p.grid), context = p.context
		, options = {
				datatype : "local",
				autowidth : true,
				height : "auto",
				deselectAfterSort : true,
				viewrecords : true,
				multiselect : true,
				toppager : true,
				userDataProxy : true,
				dataProxy : context.getJqGridDataProxy(),
				pager : p.pager,
				rowNum : context.getQueryInfo().getPageSize(),
				page : context.getQueryInfo().getPageNo(),
				rowList : [context.getQueryInfo().getPageSize()],
				colNames : p.colNames,
				colModel : p.colModel,
				onSortCol : function(index, iCol, sortorder){
					grid.jqGrid("clearGridData");
					context.getQueryInfo().setSortColumn(index + " " + sortorder);
					context.query();
					return "stop";
				},
				ondblClickRow : function(rowid, iRow, iCol, e){
					grid.jqGrid('editGridRow', rowid, {});
				}
			};
	//用户配置覆盖默认配置
	if(p.options){
		for(k in p.options){
			if(p.options.hasOwnProperty(k)){
				options[k] = p.options[k];
			}
		}
	}
	
	function initColCustom(e, f, op){
		//实现了一次新增就不允许更新
		if(options.colCustom){
			$.each(options.colCustom, function(i, v){
				if(v.editable === false){
					var ipt = f.find("input[name=" + options.colModel[i].name + "]");
					op === "add" ? ipt.removeAttr("readonly") : ipt.attr("readonly", "readonly");
				}
			});
		}
	}
	
	grid.jqGrid(options)
		.on("jqGridPagerStateChange", function(e){
			//这个时间是因为分页触发的
			var pi = grid.jqGrid("getGridParam");
			context.getQueryInfo().setPageNo(parseInt(pi.page));
			context.getQueryInfo().setPageSize(pi.rowNum);
			context.query();
		})
		.on("jqGridAddEditBeforeInitData", initColCustom)
		.on("jqGridAddEditInitializeForm", initColCustom)
		.navGrid(p.pager, {edit : true, add : true, del : true, search : false});
};

xl.libfun.auth = {
	
};