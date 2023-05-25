       	for ( i=0;i<11;i++){
       		for( j=0;j<11;j++){
       		    document.write("<button id=\""+i+","+j+"\"  class=\"button gray larrowG\" style=\" position:absolute;left:"+(25*j+50*i)+"px;top:"+(44*j)+"px;\" onclick=\"showhex('"+i+","+j+"')\"></button>");
       			if(i==0){
       			    document.write("<hr style=\" left:"+(-16+25*j+50*i)+"px;top:"+(44*j+5-10)+"px;border-bottom:3px solid #007bff8f;transform:rotate(90deg)\"/>");
       				document.write("<hr style=\" left:"+(-3+25*j+50*i)+"px;top:"+(44*j+27-10)+"px;border-bottom:3px solid #007bff8f;transform:rotate(30deg)\"/>");
       			}
       			if(i==10){
       		        document.write("<hr style=\" left:"+(36+25*j+50*i)+"px;top:"+(44*j+3.5-10)+"px;border-bottom:3px solid #007bff8f;transform:rotate(90deg)\"/>");
       				document.write("<hr style=\" left:"+(23+25*j+50*i)+"px;top:"+(44*j-18-10)+"px;border-bottom:3px solid #007bff8f;transform:rotate(30deg)\"/>");
       			}                                                 
       			if(j==0){                                         
       				document.write("<hr style=\" left:"+(-2+25*j+50*i)+"px;top:"+(44*j-18-8)+"px;border-bottom:3px solid #d62334;transform:rotate(-30deg)\"/>");
       				document.write("<hr style=\" left:"+(22+25*j+50*i)+"px;top:"+(44*j-18-8)+"px;border-bottom:3px solid #d62334;transform:rotate(30deg)\"/>");
       			}                                                                      
       			if(j==10){                                                             
       			    document.write("<hr style=\" left:"+(-2+25*j+50*i)+"px;top:"+(44*j+28-8)+"px;border-bottom:3px solid #d62334;transform:rotate(30deg)\"/>");
       				document.write("<hr style=\" left:"+(23+25*j+50*i)+"px;top:"+(44*j+28-8)+"px;border-bottom:3px solid #d62334;transform:rotate(-30deg)\"/>");
       			}
       		}
       	}