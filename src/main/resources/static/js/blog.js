let title;
let type;
let isBoolean;
let page=null;
//数据渲染
function record(records) {
    if (records.length==0){
        alert("没有查到任何数据");
    }else {
        let tbody=$(".data_list>table tbody");
        tbody.empty()
        for (let i = 0; i < records.length; i++) {
            let title= records[i].title;
            let name=records[i].type.name;
            let recommend=records[i].recommend;
            let updateTime=records[i].updateTime;
            let published=records[i].published
            let id=records[i].id
            if (recommend===true){
                recommend="是"
            }else {
                recommend="否"
            }
            if (updateTime===undefined){
                updateTime="未更新"
            }
            if (published==true){
                published="公开"
            }else {
                published="草稿"
            }
            let elem="<tr>" +
                "<td>"+id+"</td>" +
                "<td>"+title+"</td>" +
                "<td>"+name+"</td>" +
                "<td>"+recommend+"</td>" +
                "<td>"+published+"</td>" +
                "<td>"+updateTime+"</td>" +
                "<td>"+"<a href='"+"/user/blogUpdatePage/"+id+"' class='btn btn-sm  green' >修改</a><button type=\"button\" class=\"btn btn-sm  red\">删除</button>"+"</td>" +
                "</tr>";
            tbody.append(elem)
        }
    }

}
function init () {
    //博客信息初始化
    $.ajax({
        url:"/user/blogInit1",
        type:"get",
        datatype:"json",
        success:function (data) {
            let data2=JSON.parse(data)
            let records=data2.records;
            page=data2.pages;
            $("#down").val(data2.current+1)
            $("#up").val(data2.current-1)
            record(records);
        },
        error:function () {
            alert("请求错误")
        }
    })
    //博客分类信息初始化
    $.ajax({
        url:"/user/blogTypeInit",
        type:"get",
        datatype:"json",
        success:function (data) {
            $("[name=type]").empty();
            let data2=JSON.parse(data)
            $("[name=type]").append(" <option value=\"\"  selected>\n" +
                "                                分类\n" +
                "                             " +
                "                            </option>")
            for (let i = 0; i < data2.length; i++) {
                $("[name=type]").append($("<option value='"+data2[i].name+"'>"+data2[i].name+"</option>"))
            }
            $('[name=type]').trigger('chosen:updated');//更新select中的chosen插件，我想你会用到的。
            $('[name=type]').chosen();
        },
        error:function () {
            alert("请求错误")
        }
    })
}
function search() {
    $("#search").click(function () {
        title=$("[name=title]");
        type=$("[name=type]");
        isBoolean=$("[name=isBoolean]");
        let datas={};
        let type2="post";
        let url="/user/blogPage1";
        if (type.val()!=""||title.val()!=""){
            datas={"title":title.val(),"type":type.val(),"isBoolean":isBoolean.val()}
            $.ajax({
                url:url,
                type:type2,
                data:datas,
                datatype:"json",
                success:function (data) {
                    let data2=JSON.parse(data);
                    page=data2.pages;
                    let records=data2.records;
                    $("#down").val(data2.current+1)
                    $("#up").val(data2.current-1)
                    record(records);
                    if (data2.current==page){
                        let num=4-data2.records.length;
                        console.log(data2.records.length)
                        let sun=0;
                        for (let i = 0; i < num; i++) {
                            sun=sun+47;
                        }

                        $(".next-head").css({marginBottom:sun+23})
                    }
                },
                error:function () {
                    alert("请求错误")
                }
            })
        }else {
            init();
            alert("已查询");
        }
    });
}
function nextPage(){
    $("#down").click(function () {
        title=$("[name=title]");
        type=$("[name=type]");
        isBoolean=$("[name=isBoolean]");
        let datas={};
        let type2="get";
        let url="/user/blogInit"+$("#down").val();
        console.log("title;",title.val(),"type;",type.val(),"isBoolean;",isBoolean.val(),"url:",url,)
        if (type.val()!=""||title.val()!=""){
            console.log("有参数请求")
            url="/user/blogPage"+$("#down").val();
            type2="post";
            datas={"title":title.val(),"type":type.val(),"isBoolean":isBoolean.val()}
        }
        if ($("#down").val() > page) {
            alert("这是最后一页了")
        } else {
            $.ajax({
                url:url,
                type:type2,
                data:datas,
                datatype:"json",
                success:function (data) {
                    let data2=JSON.parse(data);
                    let tbody=$(".data_list>table tbody");
                    page=data2.pages;
                    tbody.empty();
                    let records=data2.records;
                    $("#down").val(data2.current+1)
                    $("#up").val(data2.current-1)
                    record(records);
                    if (data2.current==page){
                        let num=4-data2.records.length;
                        console.log(data2.records.length)
                        let sun=0;
                        for (let i = 0; i < num; i++) {
                            sun=sun+47;
                        }

                        $(".next-head").css({marginBottom:sun+23})
                    }
                },
                error:function () {
                    alert("请求错误")
                }
            })
        }
    });

}
function previousPage() {
    $("#up").click(function () {
        title=$("[name=title]");
        type=$("[name=type]");
        isBoolean=$("[name=isBoolean]");
        let datas={};
        let type2="get";
        let url="/user/blogInit"+$("#up").val();
        console.log("title;",title.val(),"type;",type.val(),"isBoolean;",isBoolean.val(),"url:",url,)
        if (type.val()!=""||title.val()!=""){
            console.log("有参数请求")
            url="/user/blogPage"+$("#up").val();
            type2="post";
            datas={"title":title.val(),"type":type.val(),"isBoolean":isBoolean.val()};
        }
        if ($("#up").val() == 0) {
            alert("这是第一页了");
        } else {
            $.ajax({
                url:url,
                type:type2,
                data:datas,
                datatype:"json",
                success:function (data) {
                    let data2=JSON.parse(data)
                    let tbody=$(".data_list>table tbody");
                    tbody.empty();
                    page=data2.pages;
                    let records=data2.records;
                    $("#down").val(data2.current+1)
                    $("#up").val(data2.current-1)
                    record(records);
                    if (data2.current!=page){
                        $(".next-head").css({marginBottom:23})
                    }
                },
                error:function () {
                    alert("请求错误")
                }
            })
        }
    });
}
$(function () {
    nextPage();
    previousPage();
    search();
})
window.setTimeout(init,300)