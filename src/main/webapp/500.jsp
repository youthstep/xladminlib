<%@page pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/G.css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/base.css">
        <style type="text/css">
            <!--
            body {
                margin-left: 0px;
                margin-top: 0px;
                margin-right: 0px;
                margin-bottom: 0px;
            }
            -->
        </style>

    </head>
    <body onload="init()">

        <table width="100%" height="100%" border="0" cellpadding="0"
               cellspacing="0">
            <tr>
                <td>
                    <table width="500" height="159" border="0" align="center"
                           cellpadding="0" cellspacing="1" style="border: 1px dashed rgb(204, 204, 204);">
                        <tr>
                            <td bgcolor="#FFFFFF">
                                <table width="100%" height="70" border="0" align="center"
                                       cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td width="17%" class="g_11">
                                            <img src="<%=request.getContextPath()%>/images/bj_14.gif"/>
                                        </td>
                                        <td width="69%" style="font-size: 12px;" align="left"
                                            id="messgetd">
                                            <strong>服务器内部错误,请稍后再尝试访问.</strong>
                                        </td>
                                        <td style="font-size: 12px;" align="center" id="tracetd">

                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" nowrap colspan="2">
                                            <input onclick="goIndex();" type="button"
                                                   value="首页" class="smb_btn" style="margin:0 20px;" />
                                            <input onclick="history.go(-1);" type="button"
                                                   value=" &#36820; &#22238; " class="smb_btn"
                                                    id="backbtn" style="display:none;margin:0 20px;"/>
                                            <input onclick="window.close()" type="button" value=" &#20851; &#38381; " class="smb_btn"  style="margin:0 20px;display:none" id="closebtn">

                                        </td>
                                        <td></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>

                </td>
            </tr>
        </table>
    </body>
    <script type="text/javascript">
        function goIndex(){
            top.location.href="<%=request.getContextPath()%>";
        }
        function hasPrevPage(){
            return history.length>1;
        }
        function init(){
            if(hasPrevPage()){
                document.getElementById("backbtn").style.display="";
                document.getElementById("closebtn").style.display="none";
            }
            else{
                document.getElementById("backbtn").style.display="none";
                document.getElementById("closebtn").style.display="";
            }
        }
    </script>
</html>