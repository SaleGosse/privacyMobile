<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['cookie']))
	{	

			if(isset($_POST['idUser']))
		 	{
			$cookie = (string)$_POST['cookie'];

		 		$dataBase =  connectionDB();
		 		$idUser = (int)$_POST['idUser'];
		 		$rq_check_cookie = "SELECT idUser FROM User WHERE cookie = :cookie AND idUser = :userID";

				//Checking the cookie, we're never too sure.
				$request = $dataBase->prepare($rq_check_cookie);
				$request->bindParam(':cookie', $cookie, PDO::PARAM_STR);
				$request->bindParam(':userID', $userID, PDO::PARAM_INT);
				$request->execute();
				///$result = $request->fetch();

				if($request->fetch())
				{
					//Printing the error
					echo "false\n";
					echo "error: Invalid cookie.";

					//Closing the database
					$dataBase = null;

					//Exiting
					exit();
				}
		 		$resultConv = $dataBase->prepare("SELECT ( SELECT count(*)  FROM Message WHERE idUser = :idUser) as 'NbMessage' ,(SELECT count(*) FROM linkConversation WHERE idUser = :idUser ) AS 'NbConversation' , (select count(idUser) from (select idConversation from linkConversation where idUser = :idUser ) a JOIN linkConversation p ON a.idConversation = p.idConversation WHERE idUser != :idUser )as Friends ");
				$resultConv->bindParam(':idUser', $idUser, PDO::PARAM_INT);
				$resultConv->execute();
				$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);

				echo json_encode($array1);

		 	}
	}


?>