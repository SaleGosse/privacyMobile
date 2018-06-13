<?php
	
	include 'checkCookie.php';

	if(isset($_POST['userID']) && isset($_POST['cookie']))
	{
		include 'connectionDB.php';

		//Getting the POST params
		$idUserSource = (int)$_POST["userID"];	
		$cookie = $_POST['cookie'];

		$dataBase =  connectionDB();
		
		if(!checkCookie($dataBase, $idUserSource, $cookie))
		{
			//Printing the error
			echo "false\n" . "error: Invalid cookie.\n";

			//Closing the db and exiting
			$dataBase = null;
			exit();
		}
		
		//Now we can get the messages
		if(isset($_POST['conversationID'])) 
		{
			$idConversation = (int)$_POST['conversationID'];

			$resultConv = $dataBase->prepare("SELECT m.idUser,lastName,firstName,m.content,m.date,m.idMessage FROM Message m Left JOIN (SELECT idUser FROM linkConversation WHERE idConversation = :idConversation ) c ON m.idUser = c.idUSer JOIN Profile p  ON m.idUser = p.idUser WHERE idConversation = :idConversation ORDER BY m.date ASC");
			$resultConv->bindParam(':idConversation', $idConversation, PDO::PARAM_INT);
			$resultConv->execute();
			$resultMessage = $resultConv->fetchAll(PDO::FETCH_ASSOC);

			$rq_update_unread = "DELETE FROM MessageStatus WHERE idMessageStatus IN (SELECT idMessageStatus FROM (SELECT ms.idMessageStatus FROM MessageStatus ms JOIN Message m ON ms.idMessage = m.idMessage WHERE m.idConversation = :conversationID) subquery)";

			$request = $dataBase->prepare($rq_update_unread);
			$request->bindParam(':conversationID', $idConversation, PDO::PARAM_INT);
			$request->execute();


			if(!empty($resultMessage))	
			{			
				echo "true" . "\n" . json_encode($resultMessage) . "\n";
			}
			else
			{
				$dataBase = null;   // Disconnect
				exit ();
			}
		}
		else
			echo "error: Invalid or missing POST parameters.\n";
	}
	else
		echo "error: Invalid or missing cookie.\n";
?>