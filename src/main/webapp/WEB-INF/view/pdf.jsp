<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script>
	var _hmt = _hmt || [];
	(function() {
	  var hm = document.createElement("script");
	  hm.src = "https://hm.baidu.com/hm.js?e495c059c321f94f833a194c3dc8c87c";
	  var s = document.getElementsByTagName("script")[0]; 
	  s.parentNode.insertBefore(hm, s);
	})();
</script>
<!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-118964508-1"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag('js', new Date());
	
	gtag('config', 'UA-118964508-1');
</script>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<meta name="description" content="PDF Viewer" />
	<title>Convert PDF To Image</title>
	<link rel="stylesheet" href="${ctx }/static/css/style.css" />
	<link rel="stylesheet" type="text/css" href="${ctx }/static/css/mian.css" />
</head>

<body>
<div class="frame">
	<div class="center">
		<div class="profile">
			<a href="https://github.com/lp1791803611/pdf2img" target="_blank">
				<div class="image">
					<div class="circle-1"></div>
					<div class="circle-2"></div>
					<img src="${ctx }/static/img/Stranger.jpg" width="70" height="70" alt="">
				</div>
			</a>

			<div class="name">PDF转换</div>
			<div class="job">文档大小   < 10Mb</div>
			<div class="actions">
				<a href="javascript:;" class="btn file">Choose Pdf
					<input id='pdf' type='file' accept="application/pdf">
				</a>
				<button class="btn" id="exportImage">Export Image</button>
				<button class="btn" id="exportPpt">Export PPT</button>
				<a style="display:block;margin-left:40px;"><iframe src="https://ghbtns.com/github-btn.html?user=lp1791803611&amp;repo=pdf2img&amp;type=star&amp;count=true&amp;size=large" frameborder="0" scrolling="0" width="170px" height="30px"></iframe></a>
			</div>
		</div>

		<div class="stats">
			<div class="box">
				<span class="value">名称</span>
				<span class="parameter" id="pdfName"></span>
			</div>
			<div class="box">
				<span class="value">大小</span>
				<span class="parameter" id="sizeText"></span>
			</div>
			<div class="box">
				<span class="value">页数</span>
				<span class="parameter" id="pagesText"></span>
			</div>
		</div>
	</div>
</div>
<div id="imgDiv"></div>
<script type="text/javascript" src="${ctx }/static/js/jszip.min.js"></script>
<script type="text/javascript" src="${ctx }/static/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${ctx }/static/js/FileSaver.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pdf.js"></script>
<script type="text/javascript" src="${ctx }/static/js/pdf.worker.js"></script>
<script>
	// PDFJS.disableStream = true;
	$("#exportImage").attr("disabled", "disabled"); //禁用导出图片按钮
	$("#exportPpt").attr("disabled", "disabled"); //禁用导出ppt按钮
	
	var pdfFile;
	$('#pdf').change(function() {
		var pdfFileURL = $('#pdf').val();
		if(pdfFileURL) {
			$("#imgDiv").empty();//清空上一PDF文件展示图
			var files = $('#pdf').prop('files'); //获取到文件
			var fileSize = files[0].size;
			var mb;
			if(fileSize) {
				mb = fileSize / 1048576;
				if(mb > 10) {
					alert("文件大小不能>10M");
					return;
				}
			}

			$("#pdfName").text(files[0].name).attr("title",files[0].name);
			$("#sizeText").text(mb.toFixed(2) + "Mb");

			/*pdf.js无法直接打开本地文件,所以利用FileReader转换*/
			var reader = new FileReader();
			reader.readAsArrayBuffer(files[0]);
			reader.onload = function(e) {
				var myData = new Uint8Array(e.target.result)
				var docInitParams = {
					data: myData
				};
				var typedarray = new Uint8Array(this.result);
				PDFJS.cMapUrl = 'https://cdn.jsdelivr.net/npm/pdfjs-dist@2.0.288/cmaps/';
				PDFJS.cMapPacked = true;
				PDFJS.getDocument(typedarray).then(function(pdf) { //PDF转换为canvas
					$("#imgDiv").css("border", "0"); //清除文本、边框
					if(pdf) {
						var pageNum = pdf.numPages;
						$("#pagesText").text(pageNum);

						for(var i = 1; i <= pageNum; i++) {
							var canvas = document.createElement('canvas');
							canvas.id = "pageNum" + i;
							$("#imgDiv").append(canvas);
							var context = canvas.getContext('2d');
							openPage(pdf, i, context);
						}
					}
				});
			};
			
			$("#exportImage").removeAttr("disabled", "disabled");
			$("#exportPpt").removeAttr("disabled", "disabled");
		}
	});

	function openPage(pdfFile, pageNumber, context) {
		var scale = 2;
		pdfFile.getPage(pageNumber).then(function(page) {
			viewport = page.getViewport(scale); // reference canvas via context
			var canvas = context.canvas;
			canvas.width = viewport.width;
			canvas.height = viewport.height;
			canvas.style.width = "100%";
			canvas.style.height = "100%";
			var renderContext = {
				canvasContext: context,
				viewport: viewport
			};
			page.render(renderContext);
		});
		return;
	};

	//dataURL转成Blob
	function dataURLtoBlob(dataurl) {
		var arr = dataurl.split(','),
			mime = arr[0].match(/:(.*?);/)[1],
			bstr = atob(arr[1]),
			n = bstr.length,
			u8arr = new Uint8Array(n);
		while(n--) {
			u8arr[n] = bstr.charCodeAt(n);
		}
		return new Blob([u8arr], {
			type: mime
		});
	}
	//导出图片
	$("#exportImage").click(function() {
		var zip = new JSZip();
		var images = zip.folder("images");
		$("canvas").each(function(index, ele) {
			var canvas = document.getElementById("pageNum" + (index + 1));
			images.file("image-" + (index + 1) + ".png", dataURLtoBlob(canvas.toDataURL("image/png", 1.0)), {
				base64: true
			});
		})
		zip.generateAsync({
			type: "blob"
		}).then(function(content) {
			saveAs(content, "pdftoimages.zip");
		});
	});
	
	function dataURLToBase64(dataUrl){
		var arr = dataUrl.split(','),
			bstr = arr[1];
		return bstr;		
	}
	//导出ppt
	$("#exportPpt").click(function() {
		createPPT();
	});
	
	function createPPT(){
		var imageArray = [];
		$("canvas").each(function(index, ele) {
			var canvas = document.getElementById("pageNum" + (index + 1));
			var bstr = dataURLToBase64(canvas.toDataURL("image/png", 1.0));
			if(bstr != null && bstr.length > 0){
				imageArray.push(bstr);
			}
		});
		var name = $("#pdfName").text();
		$.ajax({
			  url: "${ctx}/createPpt",
			  type: "post",
			  dataType: "json",
			  contentType: "application/json",
			  data:JSON.stringify({
				  pdfName:name.split(".")[0],
				  images:imageArray
			  }),
			  async: true,
			  success:function(ret){
				  window.location.href = "${ctx}/exportPpt?name="+encodeURI(encodeURI(ret.data));
			  }
		});
	}
	
</script>
</body>
</html>
