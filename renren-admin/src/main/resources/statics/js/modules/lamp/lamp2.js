$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'lamp/lamp/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '名称', name: 'name', index: 'name', width: 80 },
			{ label: '编号', name: 'num', index: 'num', width: 80 },
			// { label: '产品类别', name: 'categoryid', index: 'categoryId', width: 80 },
			{ label: '亮灭状态', name: 'status', index: 'status', width: 80 , formatter:function(value) {
			    if (value === 0) {
			        return '<span>灭灯</span>'
                } else if(value === 1) {
			        return '<span>亮灯</span>'
                }
			}
            }, 		// ，0是灭，1是亮，默认0
			{ label: '亮度', name: 'brightness', index: 'brightness', width: 80 }, 	//，默认50
			{ label: '设备是否在线', name: 'online', index: 'online', width: 80 , formatter:function(value) {
			    if (value === 1) {
			        return  '<span>离线</span>'
                } else {
			        return '<span>在线</span>'
                }
                } }, 			//，1离线，0在线，默认1
			// { label: '设备是否损坏', name: 'damage', index: 'damage', width: 80 , formatter:function(value) {
			//     if (value === 0) {
			//         return '<span>良好</span>'
            //     } else {
			//         return '<span>损坏</span>'
            //     }
            //     }}, 	  //，0没有，1损坏，默认0
			{ label: '备注', name: 'remarks', index: 'remarks', width: 80 }, 			
			{ label: '创建时间', name: 'createtime', index: 'createtime', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		lamp: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.lamp = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.lamp.id == null ? "lamp/lamp/save" : "lamp/lamp/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.lamp),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "lamp/lamp/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "lamp/lamp/info/"+id, function(r){
                vm.lamp = r.lamp;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});