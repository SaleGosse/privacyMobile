<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['cookie']))
	{	
		$cookie = (string)$_POST['cookie'];
		

		if(isset($_POST['content']) && isset($_POST['userID']) && isset($_POST['conversationID']))
		{
			$db =  connectionDB();
			
			//The different request we'll use
			$rq_check_cookie = "SELECT idUser FROM User WHERE cookie = :cookie AND idUser = :userID";
			$rq_insert_message = "INSERT INTO Message (idConversation,  idUser, content,date) VALUES (:conversationID, :userID, :content, :currentDate)";
			$rq_get_message_ID = "SELECT idMessage FROM Message WHERE idConversation = :conversationID AND idUser = :userID AND date = :currentDate";
			$rq_get_conversation_users = "SELECT idUser FROM linkConversation WHERE idConversation = :conversationID";

			//Getting the POST parameters as variables
			$conversationID = (int)$_POST['conversationID'];
			$content = $_POST["content"];
			$userID = (int)$_POST["userID"];
			
			//Checking the cookie, we're never too sure.
			$request = $db->prepare($rq_check_cookie);
			$request->bindParam(':cookie', $cookie, PDO::PARAM_STR);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->execute();
			$result = $request->fetch();

			if(!$result)
			{
				//Printing the error
				echo "false\n";
				echo "error: Invalid cookie.";

				//Closing the database
				$db = null;

				//Exiting
				exit();
			}


			$currentDate = date('Y-m-d H:i:s', time());

			// load message on server 
			$request = $db->prepare($rq_insert_message);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->bindParam(':content', $content, PDO::PARAM_STR);
			$request->bindParam(':currentDate', $currentDate, PDO::PARAM_STR);
			$request->execute();

			// take id Message 

			$request = $db->prepare($rq_get_message_ID);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->bindParam(':currentDate', $dateNow, PDO::PARAM_INT);
			$request->execute();

			$result = $request->fetchAll(PDO::FETCH_ASSOC);
			
			$messageID = $result[0]["idMessage"] ;
			
			// take all idUser inside conversation
			$request = $db->prepare($rq_get_conversation_users);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->execute();

			$result = $request->fetchAll(PDO::FETCH_ASSOC);
			
			$status = "true";

			// loop all user id and set true status of this new message
			if(!empty($result))
			{
				// set id Conversation
				//echo json_encode($result);
				/*echo $idConversation;
				echo " <br> ";*/
				/*
				foreach ($result as $key => $arr) 
				{
					$idUser = $arr["idUser"];
					if($idUser == $idSource)
					{
						$status = "false";
					}
					else
					{
						$status = "true";
					}
					// load status of message true = never see and fals = see 
					$resultTmp = $db->prepare("INSERT INTO statusMessage (idMessage,status,idUser,idConversation) VALUES (:idMessage,:status ,:idUser , :idConversation)");
					$resultTmp->bindParam(':idConversation', $idConversation, PDO::PARAM_INT);
					$resultTmp->bindParam(':idUser', $idUser, PDO::PARAM_INT);
					$resultTmp->bindParam(':idMessage', $idMessage, PDO::PARAM_INT);
					$resultTmp->bindParam(':status', $status, PDO::PARAM_STR);
					$resultTmp->execute();
				}
				*/
			}

			echo "true";


			$db = null;   // Disconnect
		}
		else 
			echo "error: Invalid or missing POST parameters.";
	}
	else
		echo "error: Missing cookie.";
?>