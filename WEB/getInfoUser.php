<?php 
	
	include 'connectionDB.php';

	//if(isset($_POST['cookie']))
	{	
		//$cookie = (string)$_POST['cookie'];

			//if(isset($_POST['idUser']))
		 	{
		 		$dataBase =  connectionDB();


		 		$idUser = 1;//$_POST['idUser'];

		 		$resultConv = $dataBase->prepare("SELECT ( SELECT count(*)  FROM Message WHERE idUser = :idUser) as 'NbMessage' ,(SELECT count(*) FROM linkConversation WHERE idUser = :idUser ) AS 'NbConversation' , (select count(idUser) from (select idConversation from linkConversation where idUser = :idUser ) a JOIN linkConversation p ON a.idConversation = p.idConversation WHERE idUser != :idUser )as Friends ");
				$resultConv->bindParam(':idUser', $idUser, PDO::PARAM_INT);
				$resultConv->execute();
				$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);

				var_dump($array1);

		 	}
	}


?>