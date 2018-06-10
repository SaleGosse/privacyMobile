<?php
	
	include 'connectionDB.php';


	if(isset($_POST['cookie']))
	{
		//Check the cookie

		//Now we can get the messages
		if(isset($_POST['userID']) && isset($_POST['conversationID'])) 
		{
		
			$dataBase =  connectionDB();

			// define username,password with post 
			
			$idUserSource = (int)$_POST["userID"];
			$idConversation = (int)$_POST['conversationID'];
			
			/*$resultConv = $dataBase->prepare("SELECT * FROM Conversation WHERE convName = :Conversation ");
			$resultConv->bindParam(':Conversation', $Conversation, PDO::PARAM_STR);
			$resultConv->execute();
			$result = $resultConv->fetchAll(PDO::FETCH_ASSOC);

			if(!empty($result))
			{
				// set id Conversation
				$idConversation = $result[0]["idConversation"];
				/*echo $idConversation;
				echo " <br> ";
			}
			else
			{
				$dataBase = null;   // Disconnect
				exit ();
			}
				*/
			/*
			$result = $dataBase->prepare(" SELECT idConversation FROM (SELECT FROM  Users_Conversation WHERE  idUser = :idUserSource) WHERE  ");
					$result->bindParam(':idUserSource', $idUserSource, PDO::PARAM_STR);
			$result->execute();
			$resultUser = $result->fetchAll(PDO::FETCH_ASSOC);
			*/

			$resultConv = $dataBase->prepare("SELECT m.idUser,lastName,firstName,m.content,m.date,m.idMessage FROM Message m Left JOIN (SELECT idUser FROM linkConversation WHERE idConversation = :idConversation ) c ON m.idUser = c.idUSer JOIN Profile p  ON m.idUser = p.idUser WHERE idConversation = :idConversation ORDER BY m.date ASC");
			$resultConv->bindParam(':idConversation', $idConversation, PDO::PARAM_INT);
			$resultConv->execute();
			$resultMessage = $resultConv->fetchAll(PDO::FETCH_ASSOC);

			//var_dump($resultMessage);
			// check if you have somethings inside array
			if(!empty($resultMessage))	
			{
				/*foreach ($resultMessage as $key => $arr) 
				{
					// $arr[3] will be updated with each value from $arr...
		    		//echo "{$key} => {}";
		    		//var_dump($arr);
		    		$ouais =  date('m/d/Y H:i:s', $arr["date"]);
		    	
		    		$resultMessage[$key]["date"] = $ouais;
		    		$idMessage = $arr["idMessage"];
		    		$result = $dataBase->prepare("UPDATE statusMessage SET status = 'false' WHERE idConversation = :idConversation AND idUser = :idUserSource AND  idMessage = :idMessage AND status = 'true' ");
					$result->bindParam(':idConversation', $idConversation, PDO::PARAM_INT);
					$result->bindParam(':idUserSource', $idUserSource, PDO::PARAM_INT);
					$result->bindParam(':idMessage', $idMessage, PDO::PARAM_INT);
					$result->execute();
		    	}*/
			
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