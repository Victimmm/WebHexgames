function log_out(){
	var keys = document.cookie.match(/[^ =;]+(?==)/g)
	if (keys) {
	    for (var i = keys.length; i--;) {
	      document.cookie = keys[i] + '=0;path=/;expires=' + new Date(0).toUTCString() // 清除当前域名下的,例如：m.ratingdog.cn
	      document.cookie = keys[i] + '=0;path=/;domain=' + document.domain + ';expires=' + new Date(0).toUTCString() // 清除当前域名下的，例如 .m.ratingdog.cn
	      document.cookie = keys[i] + '=0;path=/;domain=ratingdog.cn;expires=' + new Date(0).toUTCString() // 清除一级域名下的或指定的，例如 .ratingdog.cn
	    }
	  }
	//获取当前网址，如： http://localhost:8080/index
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： /index
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPath = curWwwPath.substring(0, pos);
	location.href = localhostPath+"/index";
}