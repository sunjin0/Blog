
$(function(){
  $(function () {
    $('[data-toggle="popover"]').popover()
  })
  $(function () {
    $('[data-toggle="tooltip"]').tooltip()
  })
//用户栏
  $(".author").mouseenter(function() {
    $(".user_info").show(100)
 });
 $(".author").mouseleave(function() {
    $(".user_info").hide(100)
 })

//底部回顶
function scrool_blog(params) {
  if ( $(document).scrollTop()>100) {
   $(".top-back").show(100)
  }
  if ( $(document).scrollTop()==0) {
   $(".top-back").hide(100)
  }

}
$(window).scroll( scrool_blog)
$(".top-back").click(function (params) {
   $(document).scrollTop(0)
})
//菜单栏
var isClick=false;
$(".cataloy-btn").click(function(){
  if (isClick==false) {
      $(".cataloy").show(100);
     isClick=true;
  }else if(isClick==true){
      $(".cataloy").hide(100);
     isClick=false;
  }
 });
 //目录
 var hs=$(".typo [id^='h']");
 for (let index = 0; index < hs.length; index++) {
  const element = hs[index];
  $(".cataloy").append(' <li class="list-group-item"><a href="#'+element.id+'">'+element.innerText+'</a></li>')
 }
 //非空验证
$("#login").click(function () {
   if ( $("#User").val()==""||$("#Password").val()==""){
       alert("请输入账号或密码!")
       return false;
   }
});
 //是否推荐
    let clicked=true;
    let isBoolean=$("[name=isBoolean]");
    isBoolean.click(function () {
        if (clicked === true){
            isBoolean.val(false)
            clicked=false;
        }else if (clicked === false){
            isBoolean.val(true)
            clicked=true;
        }
    });
    $("body").on("click",".close_text",function(){
        $(".alert").hide();
    })
    //退出系统
    $("body").on("click",".zx",function () {
        $.ajax({
            url:"/user/logout",
            type:"get",
            success:function (data) {
                location.href="/"
            },
            error:function () {
                alert("出错了")
            }
        })
    })
    //搜索提示
    $("#search_text").bind("input propertychange",function () {
        if ($(this).val()!=""){
            $.ajax({
                url:"/search_hint"+$("#search_text").val(),
                type:"get",
                dataType:"json",
                success:function (data) {
                    $(".search_hint").empty()
                    if(data.length!=0){
                        for (let i = 0; i < data.length; i++) {
                            let elem=$("<li></li>").attr("value", data[i].title)
                            let a=$("<a></a>").attr("href","/blogDetails/"+data[i].id).attr("target","_blank").text( data[i].title)
                            elem.append(a)
                            $(".search_hint").append(elem)

                        }
                    }else{
                        $(".search_hint").append("<li>没有查到任何数据</li>")
                    }
                },
                error:function () {
                    alert("出错了")
                }
            })
        }
    })
    //点击该盒子外的空间关闭这个窗口，点击这个盒子不会关闭
    $("body").click(function (e) {
        if (!$(e.target).closest(".search_hint").length)
            $(".search_hint").empty()
    })
})