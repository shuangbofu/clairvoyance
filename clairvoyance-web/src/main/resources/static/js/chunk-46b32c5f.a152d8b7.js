(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-46b32c5f"],{"12bb":function(t,e,a){"use strict";var n=a("1aba"),o=a.n(n);o.a},"1aba":function(t,e,a){},2532:function(t,e,a){"use strict";var n=a("23e7"),o=a("5a34"),i=a("1d80"),s=a("ab13");n({target:"String",proto:!0,forced:!s("includes")},{includes:function(t){return!!~String(i(this)).indexOf(o(t),arguments.length>1?arguments[1]:void 0)}})},"48f8":function(t,e,a){"use strict";var n=a("994e"),o=a.n(n);o.a},"5a34":function(t,e,a){var n=a("44e7");t.exports=function(t){if(n(t))throw TypeError("The method doesn't accept regular expressions");return t}},"6a7e":function(t,e,a){},"6e21":function(t,e,a){"use strict";var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"dashboard-container"},[a("a-row",{attrs:{type:"flex",justify:"center",align:"top",gutter:16}},[a("a-col",{attrs:{xs:{span:7},xl:{span:5},xxl:{span:5}}},[a("div",{staticClass:"catalogue-container"},[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[a("a-input",{attrs:{placeholder:"过滤",icon:"FilterOutlined"},model:{value:t.filterValue,callback:function(e){t.filterValue=e},expression:"filterValue"}},[a("a-icon",{attrs:{slot:"suffix",type:"filter"},slot:"suffix"})],1),a("a-dropdown",{attrs:{trigger:["click"]}},[a("a-menu",{attrs:{slot:"overlay"},on:{click:t.handleMenuClick},slot:"overlay"},[a("a-menu-item",{key:"文件夹"},[a("a-icon",{attrs:{type:"folder"}}),t._v("创建文件夹 ")],1),a("a-menu-item",{key:"仪表盘"},[a("a-icon",{attrs:{type:"dashboard"}}),t._v("创建仪表盘 ")],1)],1),a("div",[a("a-button",{staticStyle:{"margin-left":"5px"},attrs:{icon:"plus"}})],1)],1)],1),a("div",{staticClass:"catalogue-tree"},[t.treeData.length>0?a("a-tree",{attrs:{"show-icon":"","tree-data":t.filteredTreeData,expandedKeys:t.defaultExpandedKeys,selectedKeys:t.defaultSelectedKeys},on:{expand:t.expandNode,select:t.selectNode,"update:expandedKeys":function(e){t.defaultExpandedKeys=e},"update:expanded-keys":function(e){t.defaultExpandedKeys=e},"update:selectedKeys":function(e){t.defaultSelectedKeys=e},"update:selected-keys":function(e){t.defaultSelectedKeys=e}},scopedSlots:t._u([{key:"folder",fn:function(t){var e=t.expanded;return[a("a-icon",{staticClass:"tree-icon",attrs:{theme:"outlined",type:e?"folder-open":"folder"}})]}},{key:"title",fn:function(e){return[e.folder?[t._v(t._s(e.title))]:a("a-tooltip",{attrs:{overlayClassName:"tooltip",placement:"right",overlayStyle:{background:"#fff"}}},[a("template",{slot:"title"},["dashboard"===t.item?[a("div",[t._v("名称："+t._s(e.title))]),a("div",[t._v("备注："+t._s(e.remarks))])]:t._e(),"workSheet"===t.item?[a("div",[t._v("名称："+t._s(e.title))]),a("div",[t._v("原名："+t._s(e.tableName))]),a("div",[t._v("描述："+t._s(e.description))])]:t._e()],2),t._v(" "+t._s(e.title)+" ")],2)]}}],null,!1,2447245915)},[a("a-icon",{staticClass:"tree-icon",attrs:{slot:t.item,theme:"outlined",type:t.treeIcon},slot:t.item})],1):t._e()],1)])]),a("a-col",{attrs:{xs:{span:17},xl:{span:19},xxl:{span:19}}},[a("div",{staticClass:"right-container"},[t.dashboardLoading?a("a-spin",{staticClass:"spin",attrs:{tip:"仪表盘加载中……"}}):[t._t("right-container")]],2)])],1),a("a-modal",{attrs:{title:"新建"+t.menuKey,visible:t.visible,"confirm-loading":t.confirmLoading,destroyOnClose:!0},on:{ok:t.handleOk,cancel:function(){t.visible=!1,t.form={parentId:""}}}},[a("a-form-model",t._b({model:{value:t.form,callback:function(e){t.form=e},expression:"form"}},"a-form-model",{labelCol:{span:6},wrapperCol:{span:16}},!1),[a("a-form-model-item",{attrs:{label:"所属文件夹"}},[a("a-tree-select",{staticStyle:{width:"100%"},attrs:{"dropdown-style":{maxHeight:"400px",overflow:"auto"},"tree-data":t.selectedTreeData,"tree-default-expand-all":""},model:{value:t.form.parentId,callback:function(e){t.$set(t.form,"parentId",e)},expression:"form.parentId"}})],1),a("a-form-model-item",{attrs:{label:t.menuKey+"名称"}},[a("a-input",{model:{value:t.form.name,callback:function(e){t.$set(t.form,"name",e)},expression:"form.name"}})],1),"仪表盘"==t.menuKey?a("a-form-model-item",{attrs:{label:t.menuKey+"备注"}},[a("a-input",{attrs:{type:"textarea"},model:{value:t.form.remarks,callback:function(e){t.$set(t.form,"remarks",e)},expression:"form.remarks"}})],1):t._e()],1)],1)],1)},o=[],i=(a("4de4"),a("4160"),a("caad"),a("d81d"),a("b0c0"),a("2532"),a("159b"),a("5530"));function s(t,e){localStorage.setItem(t,JSON.stringify(e))}function r(t){return JSON.parse(localStorage.getItem(t))}function c(t){return t.filter((function(t){return t.folder})).map((function(t){return Object(i["a"])(Object(i["a"])({},t),{},{selectable:!0,children:c(t.children)})}))}function d(t,e){var a=[];return t.forEach((function(t){if(t.isLeaf)t.title.includes(e)&&a.push(t);else{var n=Object(i["a"])(Object(i["a"])({},t),{},{children:d(t.children,e)});n.children.length>0&&a.push(n)}})),a}var l={props:["item"],data:function(){return{treeData:[],defaultExpandedKeys:[],defaultSelectedKeys:[],filterValue:"",visible:!1,confirmLoading:!1,menuKey:"",form:{parentId:""},dashboard:{},dashboardLoading:!0}},created:function(){this.init()},components:{},computed:{selectedKeysLocalKey:function(){return"".concat(this.item,"_selectedKeys")},expandedKeysLocalKey:function(){return"".concat(this.item,"_expandedKeys")},selectedRefIdKey:function(){return"".concat(this.item,"_ref_id")},filteredTreeData:function(){return d(this.treeData,this.filterValue)},selectedTreeData:function(){return c(this.treeData)},treeIcon:function(){return"workSheet"===this.item?"table":this.item}},methods:{init:function(){var t=this;this.defaultSelectedKeys=r(this.selectedKeysLocalKey),this.defaultExpandedKeys=r(this.expandedKeysLocalKey);var e=r(this.selectedRefIdKey);this.$axios.get("/".concat(this.item,"/catalogue")).then((function(a){var n=[{name:"根目录",id:0,refId:0,folder:!0,children:a}],o=n.map((function(e){return t.convert2Node(e)}));t.treeData=o,null!=e?t.chooseDashboard(e):t.dashboardLoading=!1}))},selectNode:function(t,e){var a=e.node;s(this.selectedKeysLocalKey,t),this.selectedKeys=t,a.dataRef.folder||(s(this.selectedRefIdKey,a.dataRef.refId),a.selected||this.chooseDashboard(a.dataRef.refId))},expandNode:function(t){s(this.expandedKeysLocalKey,t)},handleMenuClick:function(t){this.menuKey=t.key,this.visible=!0},chooseDashboard:function(t){this.dashboardLoading=!0,this.$emit("choose",t)},loaded:function(){this.dashboardLoading=!1},handleOk:function(){var t=this;if(""!==this.form.parentId){var e="/".concat(this.item);"文件夹"===this.menuKey&&(e+="/folder"),this.confirmLoading=!0,this.$axios.post(e,this.form).then((function(e){e&&(t.confirmLoading=!1,t.$message.success("创建".concat(t.menuKey,"成功！")),t.visible=!1,"文件夹"!==t.menuKey&&(s(t.selectedKeysLocalKey,["node_".concat(e.nodeId)]),s(t.selectedRefIdKey,e.dashboardId)),t.defaultExpandedKeys.push("node_".concat(t.form.parentId)),s(t.expandedKeysLocalKey,t.defaultExpandedKeys),t.init())})).catch((function(e){console.log(e),t.confirmLoading=!1}))}else this.$message.error("所属文件夹不能为空！")},convert2Node:function(t){var e=this,a=Object(i["a"])(Object(i["a"])({title:t.name},t),{},{key:"node_"+t.id,value:t.id,isLeaf:!t.folder||0===t.children.length,children:t.children.map((function(t){return e.convert2Node(t)})),selectable:!t.folder});return t.folder?a.scopedSlots={icon:"folder",title:"title"}:a.scopedSlots={icon:this.item,title:"title"},a}}},u=l,f=(a("6e4a"),a("2877")),h=Object(f["a"])(u,n,o,!1,null,null,null);e["a"]=h.exports},"6e4a":function(t,e,a){"use strict";var n=a("6a7e"),o=a.n(n);o.a},"994e":function(t,e,a){},ab13:function(t,e,a){var n=a("b622"),o=n("match");t.exports=function(t){var e=/./;try{"/./"[t](e)}catch(a){try{return e[o]=!1,"/./"[t](e)}catch(n){}}return!1}},caad:function(t,e,a){"use strict";var n=a("23e7"),o=a("4d64").includes,i=a("44d2"),s=a("ae40"),r=s("indexOf",{ACCESSORS:!0,1:0});n({target:"Array",proto:!0,forced:!r},{includes:function(t){return o(this,t,arguments.length>1?arguments[1]:void 0)}}),i("includes")},ed52:function(t,e,a){"use strict";a.r(e);var n=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("catalogue",{ref:"catRef",attrs:{item:"dashboard"},on:{choose:t.chooseDashboard}},[a("template",{slot:"right-container"},[t.dashboard?a("div",{staticClass:"dashboard-main"},[a("div",{staticClass:"dashboard-header"},[a("div",{staticClass:"title"},[t._v(" "+t._s(t.dashboard.name)+" "),a("div",{staticClass:"remarks"},[t._v(t._s(t.dashboard.remarks))])]),a("div",{staticClass:"button-list"},[a("a-button",{attrs:{icon:"bar-chart"},on:{click:t.addChart}},[t._v("添加图表")]),a("a-button",{attrs:{icon:"highlight"}},[t._v("设计")]),a("a-button",{attrs:{icon:"fullscreen"}},[t._v("全屏")]),a("a-button",{attrs:{icon:"share-alt"}},[t._v("分享")]),a("a-button",{attrs:{icon:"more"}},[t._v("更多")])],1)]),a("div",{staticClass:"charts-container"},[0===t.charts.length?[a("div",{staticClass:"empty-block",on:{click:t.addChart}},[a("div",{staticStyle:{margin:"30px"}},[a("a-icon",{staticClass:"icon",attrs:{type:"plus"}}),a("div",[t._v("添加图表")])],1)])]:t._l(t.charts,(function(t){return a("chart-item",{key:t.id,attrs:{conf:t}})}))],2)]):t._e()])],2),a("a-modal",{attrs:{title:"添加图表",visible:t.visible,"confirm-loading":t.confirmLoading,destroyOnClose:!0},on:{ok:t.handleOk,cancel:function(){t.visible=!1,t.form={}}}},[a("a-form-model-item",{attrs:{label:"选择工作表"}},[a("a-select",{staticStyle:{width:"100%"},attrs:{"show-search":"","filter-option":t.filterOption},model:{value:t.form.workSheetId,callback:function(e){t.$set(t.form,"workSheetId",e)},expression:"form.workSheetId"}},t._l(t.workSheets,(function(e){return a("a-select-option",{key:e.workSheetId,attrs:{value:e.workSheetId}},[t._v(t._s(e.title))])})),1)],1)],1)],1)},o=[],i=(a("c975"),a("5530")),s=a("6e21"),r=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"chart-container"},[a("div",{staticClass:"chart-header"},[a("div",{staticClass:"title"},[t._v(t._s(t.conf.name))]),a("div",{staticClass:"button-list"},[a("a-icon",{staticClass:"button",attrs:{type:"edit"},on:{click:function(e){return t.$router.push({name:"编辑图表",query:{chartId:t.conf.chartId,workSheetId:t.conf.workSheetId}})}}}),a("a-icon",{staticClass:"button",attrs:{type:"redo"},on:{click:t.fetchData}}),a("a-icon",{staticClass:"button",attrs:{type:"more"}})],1)]),a("chart-box",{staticStyle:{position:"relative",width:"calc(100% - 10px)",height:"300px"},attrs:{chart:t.conf,data:t.chartData}})],1)},c=[],d=a("9677"),l={props:["conf"],data:function(){return{chartData:[]}},created:function(){this.fetchData()},components:{ChartBox:d["a"]},methods:{fetchData:function(){var t=this;this.$axios.get("/chart/data?chartId=".concat(this.conf.chartId),{timeout:1e5}).then((function(e){t.chartData=e}))}}},u=l,f=(a("48f8"),a("2877")),h=Object(f["a"])(u,r,c,!1,null,null,null),m=h.exports,p={data:function(){return{dashboard:null,visible:!1,confirmLoading:!1,workSheets:[],form:{}}},components:{Catalogue:s["a"],ChartItem:m},computed:{charts:function(){return this.dashboard.charts}},methods:{chooseDashboard:function(t){var e=this;this.$axios.get("/dashboard?id=".concat(t)).then((function(t){e.dashboard=t,e.$refs.catRef.loaded()}))},addChart:function(){var t=this;this.visible=!0,this.$axios.get("/workSheet/list").then((function(e){t.workSheets=e}))},handleOk:function(){var t=this;this.confirmLoading=!0,this.$axios.post("/chart",Object(i["a"])(Object(i["a"])({},this.form),{},{dashboardId:this.dashboard.dashboardId})).then((function(e){t.$message.success("创建成功！"),t.visible=!1,t.confirmLoading=!1,t.$emit("refresh"),t.$router.push({name:"编辑图表",query:{chartId:e,workSheetId:t.form.workSheetId}})})).catch((function(){t.confirmLoading=!1}))},filterOption:function(t,e){return e.componentOptions.children[0].text.toLowerCase().indexOf(t.toLowerCase())>=0}}},v=p,b=(a("12bb"),Object(f["a"])(v,n,o,!1,null,null,null));e["default"]=b.exports}}]);
//# sourceMappingURL=chunk-46b32c5f.a152d8b7.js.map