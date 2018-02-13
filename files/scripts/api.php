<?php 
 
 require_once 'DBconnection.php';
 
 $response = array();
 
 if(isset($_GET['apicall'])){
 
 switch($_GET['apicall']){
 
 case 'signup':
 if(isTheseParametersAvailable(array('username','email','password','gender'))){
 $username = $_POST['username']; 
 $email = $_POST['email']; 
 $password = md5($_POST['password']);
 $gender = $_POST['gender']; 
 
 $stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
 $stmt->bind_param("ss", $username, $email);
 $stmt->execute();
 $stmt->store_result();
 
 if($stmt->num_rows > 0){
 $response['error'] = true;
 $response['message'] = 'User already registered';
 $stmt->close();
 }else{
 $stmt = $conn->prepare("INSERT INTO users (username, email, password, gender) VALUES (?, ?, ?, ?)");
 $stmt->bind_param("ssss", $username, $email, $password, $gender);
 
 if($stmt->execute()){
 $stmt = $conn->prepare("SELECT id, id, username, email, gender FROM users WHERE username = ?"); 
 $stmt->bind_param("s",$username);
 $stmt->execute();
 $stmt->bind_result($userid, $id, $username, $email, $gender);
 $stmt->fetch();
 
 $user = array(
 'id'=>$id, 
 'username'=>$username, 
 'email'=>$email,
 'gender'=>$gender
 );
 
 $stmt->close();
 
 $response['error'] = false; 
 $response['message'] = 'User registered successfully'; 
 $response['user'] = $user; 
 }
 }
 
 }else{
 $response['error'] = true; 
 $response['message'] = 'required parameters are not available'; 
 }
 
 break; 
 
 case 'login':
 
 if(isTheseParametersAvailable(array('username', 'password'))){
 
 $username = $_POST['username'];
 $password = md5($_POST['password']); 
 
 $stmt = $conn->prepare("SELECT id, username, email, gender FROM users WHERE username = ? AND password = ?");
 $stmt->bind_param("ss",$username, $password);
 
 $stmt->execute();
 
 $stmt->store_result();
 
 if($stmt->num_rows > 0){
 
 $stmt->bind_result($id, $username, $email, $gender);
 $stmt->fetch();
 
 $user = array(
 'id'=>$id, 
 'username'=>$username, 
 'email'=>$email,
 'gender'=>$gender
 );
 
 $response['error'] = false; 
 $response['message'] = 'Login successfull'; 
 $response['user'] = $user; 
 }else{
 $response['error'] = false; 
 $response['message'] = 'Invalid username or password';
 }
 }
 break; 
 
 
 case 'newMeet':
		if(isTheseParametersAvailable('username','email','fecha','horario','motivo','nombre_institucion', 'num_personas')){
			$username = $_POST['username'];
			$email = $_POST['email'];
			$fecha = $_POST['fecha'];
			$horario = $_POST['horario'];
			$motivo = $_POST['motivo'];
			$nombre_institucion = $_POST['nombre_institucion'];
			$num_personas = $_POST['num_personas'];
			
			$stmt = $conn->prepare
			("SELECT id_reserva FROM reserva WHERE username = ?");
			
			$stmt->bind_param("s",$username);
			$stmt->execute();
			$stmt->store_result();
			
			if($stmt->num_rows > 0){
				$response['error']=true;
				$response['message']='reserva ya registrada';
				$stmt->close();
			}else{
				$stmt = $conn->prepare
			("INSERT INTO reserva(username, email, fecha, horario, motivo, nombre_institucion, num_personas) VALUES (?, ?, ?, ?, ?, ?, ?)"); 
				$stmt->bind_param("sssssss",$username, $email, $fecha, $horario, $motivo, $nombre_institucion, $num_personas);
				if($stmt->execute()){
					$stmt = $conn->prepare("SELECT id_reserva, id_reserva, username, email, fecha, horario, motivo, nombre_institucion, num_personas 
					FROM reserva WHERE username = ?");
					$stmt->bind_param("s",$username);
					$stmt->execute();
					$stmt->bind_result($id_reserva, $id, $username, $email, $fecha, $horario, $motivo, $nombre_institucion, $num_personas);
					$stmt->fetch();
					
					$meet = array(
						'id'=>$id,
						'username'=>$username,
						'email'=>$email,
						'fecha'=>$fecha,
						'horario'=>$horario,
						'motivo'=>$motivo,
						'nombre_institucion'=>$nombre_institucion,
						'num_personas'=>$num_personas
					);
					
					$stmt->close();
					$response['error']=false;
					$response['message']='su reserva se realizó con éxito';
				}
			}	
		}else{
			$response['error'] = true;
			$response['message'] = 'los argumentos no estan disponibles';
		}
		break;
 
 
 default: 
 $response['error'] = true; 
 $response['message'] = 'Invalid Operation Called';
 }
 
 }else{
 $response['error'] = true; 
 $response['message'] = 'Invalid API Call';
 }
 
 echo json_encode($response);
 
 function isTheseParametersAvailable($params){
 
 foreach($params as $param){
 if(!isset($_POST[$param])){
 return false; 
 }
 }
 return true; 
 }