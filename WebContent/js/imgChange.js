 function previewImage(targetObj, View_area) {


      var idValueEx  = getExtensionOfFilename(targetObj.value);//확장자명을 가져옴.
			//alert(targetObj.value)

      var preview = document.getElementById(View_area); //div id

      var ft = checkTitleFileType(idValueEx);//이미지 타입인지 확인함
      		
      var oriImg = document.getElementById("oriImg");
      if(oriImg){
      		preview.removeChild(oriImg);
      }
           var files = targetObj.files;
				  	console.log(files)
		
           for (var i = 0; i < files.length; i++) {
               var file = files[i];
               var imageType = /image.*/; //이미지 파일일경우만.. 뿌려준다.

               if (!file.type.match(imageType))
                   continue;

               var prevImg = document.getElementById("prev_" + View_area); //이전에 미리보기가 있다면 삭제
               
               if (prevImg) {
                   preview.removeChild(prevImg);
               }

               var img = document.createElement("img");
               img.id = "prev_" + View_area;
               img.classList.add("obj");
               img.file = file;
               
               if(View_area == "previewId0"){
                  
                  img.style.maxWidth  = '480px';
                  
               }else{
                  img.style.maxWidth  = '768px';
                  
               }
               
               
            /*  img.style.height = '200px'; */
               
           
               
               preview.appendChild(img);
               if (window.FileReader) { // FireFox, Chrome, Opera 확인.
                   var reader = new FileReader();
                   reader.onloadend = (function(aImg) {
                       return function(e) {
                           aImg.src = e.target.result;
                       };
                   })(img);
                   reader.readAsDataURL(file);
               } else { // safari is not supported FileReader
                   //alert('not supported FileReader');
                   if (!document.getElementById("sfr_preview_error_"
                           + View_area)) {
                       var info = document.createElement("p");
                       info.id = "sfr_preview_error_" + View_area;
                       info.innerHTML = "not supported FileReader";
                       preview.insertBefore(info, null);
                   }
               }
           }
       
   
      
}

   
   //확장자명을 가져옴.
   function getExtensionOfFilename(filename) { 
      var _fileLen = filename.length; 
      
      var _lastDot = filename.lastIndexOf('.');
      var _fileExt = filename.substring(_lastDot, _fileLen).toLowerCase(); 
	  return _fileExt; 
      
      
      
   }


   //이미지파일인지 확인
   function checkTitleFileType(obj){ 

      filetype = obj; 
      
      if (filetype == '.gif'|| filetype == '.jpg' || filetype == '.jpeg' || filetype == '.png'){
            return true; 
      }else{
            return false; 
      } 
      
      
      }