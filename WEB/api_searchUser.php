<?php 
	
	include 'connectionDB.php';

//	if(isset($_POST['cookie']))
	{	

			//if(isset($_POST['idUser']) && isset($_POST['friendName']))
		 	{
				$dataBase =  connectionDB();

				/*$cookie = (string)$_POST['cookie'];
		 		$rq_check_cookie = "SELECT idUser FROM User WHERE cookie = :cookie AND idUser = :userID";

				$request = $dataBase->prepare($rq_check_cookie);
				//Checking the cookie, we're never too sure.
				$request->bindParam(':cookie', $cookie, PDO::PARAM_STR);
				$request->bindParam(':userID', $userID, PDO::PARAM_INT);
				$request->execute();

				if($request->fetch())
				{
					//Printing the error
					echo "false\n";
					echo "error: Invalid cookie.";

					//Closing the database
					$dataBase = null;

					//Exiting
					exit();
				}*/
	
	 			// set id friend
	 			$loginFriend = (string)$_POST['friendName'];
	 			$idUserSource = (int)$_POST['idUser'];
				$result = $dataBase->prepare("SELECT idUser FROM User WHERE login = :login");
				$result->bindParam(':login', $login, PDO::PARAM_STR);
				$result->execute();
				$idUser = $result->fetchAll(PDO::FETCH_ASSOC);
				$a = $idUser[0]['idUser'];
				$idUserFriend = $a;
				// it's my
				if($idUserFriend == $idUserSource)
				{
					echo "flase";
					exit ();
				}

				// check already my followers
				$resultConv = $dataBase->prepare("");
					$resultConv->bindParam(':idUserSource', $idUserSource, PDO::PARAM_INT);
					$resultConv->execute();
					$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);


				 if 
				{

					$resultConv = $dataBase->prepare("SELECT ( SELECT count(*)  FROM Message WHERE idUser = :idUserSource) as 'NbMessage' ,(SELECT count(*) FROM linkConversation WHERE idUser = :idUserSource ) AS 'NbConversation' , (select count(idUserFriend) from Friend WHERE idUser = :idUserSource )as 'Friends' ");
					$resultConv->bindParam(':idUserSource', $idUserSource, PDO::PARAM_INT);
					$resultConv->execute();
					$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);

					//echo json_encode($array1);
					$arr = json_decode(json_encode($array1));
 					$arr[1]['code'] = "myProfile";
					$array1 = json_encode($arr);

 					echo $array1 ; 
				}
		 		// check if already friend

		 		/*$resultConv = $dataBase->prepare("SELECT ( SELECT count(*)  FROM Message WHERE idUser = :idUser) as 'NbMessage' ,(SELECT count(*) FROM linkConversation WHERE idUser = :idUser ) AS 'NbConversation' , (select count(idUserFriend) from Friend WHERE idUser = :idUser )as 'Friends' ");
				$resultConv->bindParam(':idUser', $idUser, PDO::PARAM_INT);
				$resultConv->execute();
				$array1 = $resultConv->fetchall(PDO::FETCH_OBJ);

				echo json_encode($array1);
				*/
		 	}
	}


?>