	
<!DOCTYPE html>

<html lang="fr-FR">

	<head>
		<title> OuaisOuais  </title>
		<link rel="stylesheet" type="text/css" href="/style/style.css" />
		<script type="text/javascript" src="/javascript/javascript.js"></script>
		<link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.min.css"/>
		<script src="/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="/jquery.js"></script>
	</head>

	<body class="index" >
		<div class="parentPepito">
			<div class="pepito">
		 		<div class="pepitoInput">
					<label > Message  </label>
		 			
			 		<input type="text" class="Message" name="Message" placeholder="Message" required /> 

		    	 		<label> UserSource	</label>
					<input type="text" class="Phone_Number" name="UserSource" placeholder="Phone Number" required />

						<label> UserDestination	</label>
					<input type="text" class="Phone_Number" name="UserDestination" placeholder="Phone Number" required />		
		 		</div>
		 		
				<div class="relative3">
	    			<input class="inputSend" type="submit" value="Send" onclick="sendMessage()" />
				</div>
			</div>
		</div>
		
		<div id="retour"> </div> 
		<script type="text/javascript"> 
			function sendMessage()
 			{
				var username = document.getElementsByClassName("inputUsername");
				var phoneNumber = document.getElementsByClassName("Phone_Number");
     			
     			$.ajax({
		            url : '/php/authenticate.php',
		            type : 'POST',
		            data : 'username=' + username[0].value  + '&phoneNumber=' + phoneNumber[0].value,
            		success : function(code_html){
            			$('#retour').html(code_html);
            		}
        		});   
 			}
		</script>
	</body>	
</html>
