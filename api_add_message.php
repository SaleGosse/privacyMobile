<?php 
	
	include 'connectDB.php';

	if(isset($_POST['Message']) &&  isset($_POST['Phone_number_Source']) &&  isset($_POST['Phone_number_Destination'])) 
	{
		$dataBase =  connectionDB();
		// define username,password with post 
		$message = $_POST["Message"];
		$phone_number_Source = $_POST["Phone_number_Source"];
		$Phone_number_Destination = $_POST['Phone_number_Destination'];



		$result = $dataB->prepare("SELECT idUser FROM  User WHERE  idUser = :phone_number_Source OR idUser = :Phone_number_Destination ");
		$result->bindParam(':phone_number_Source', $phone_number_Source, PDO::PARAM_STR);
		$result->execute();

		$idUser = $result->fetchAll(PDO::FETCH_ASSOC);

		if($idUser)
		{
			$idSource = $idUser[0]["idUser"];
			$idDestination = $idUser[0]["idUser"];
			$result = $dataB->prepare("INSERT INTO Message (containte, idUserSource, idUserDestinatin ) VALUES ( :message, :idSource, :idDestination)");
			$result->bindParam(':message', $message, PDO::PARAM_STR);
			$result->bindParam(':idSource', $idSource, PDO::PARAM_STR);
			$result->bindParam(':idDestination', $idDestination, PDO::PARAM_STR);	
			$result->execute();
		}
		
		$dataBase = null;   // Disconnect

	}
?>