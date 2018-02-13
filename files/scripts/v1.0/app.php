<?php

require_once 'conexion.php';

$response = array();

if(isset($_GET['apicall'])){
	switch($_GET['apicall']){
		case 'signup':
		if(isTheseParametersAvailable(array('username','email','password','gender'))){
			$username = $_POST['username'];
			$email = $_POST['email'];
			$password = md5($_POST['password']);
			$gender = $_POST['gender'];
			
			//preparar la consulta, obtener id a partir del nombre de usuario o correo
			$stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
			$stmt->bind_param("ss", $username, $email);
			$stmt->execute();
			$stmt->store_result();
			
			//en caso de que hayan dos registros con el mismo nombre o el mismo correo, se despliega un mensaje de error
			if($stmt->num_rows > 0){
				$response['error'] = true;
				$response['message'] = 'Usuario ya registrado';
				$stmt->close();
			
			//en caso de que no existan registros previos sobre un registro
			}else{
				
				//preparar la consulta para insertar un nuevo registro de usuario
				$stmt = $conn->prepare("INSERT INTO users (username, email, password, gender) VALUES (?, ?, ?, ?)");
				$stmt->bind_param("ssss", $username, $email, $password, $gender);
				
				//si logra ejecutarse la consulta toma como resultados la respuesta y  los almacena en un arreglo
				if($stmt->execute()){
					$stmt = $conn->prepare("SELECT id, id, username, email, gender FROM users WHERE username = ?");
					$stmt->bind_param("s", $username);
					$stmt->execute();
					$stmt->bind_result($userid, $id, $username, $email, $gender);
					$stmt->fetch();
					
					//almacenados en un arreglo
					$user = array()(
						'id'=>$id,
						'username'=>$username,
						'email'=>$email,
						'gender'=>$gender
					);
					
					$stmt->close();
					
					//mensaje de confirmación de un nuevo usuario registrado
					$response['error'] = false;
					$response['message'] = 'Usuario registrado con exito';
					$response['user'] = $user;
				}
			}
		//en caso de que los argumentos no sean los correctos	
		}else{
			$response['error'] = true;
			$response['message'] = 'informacion no disponible';
		}
		
		break;
		
		case 'login':
		if(isTheseParametersAvailable(array('username','password'))){
			$username = $_POST['username'];
			$password = md5($_POST['password']);
			
			//prepara la consulta tomando en cuenta solo el nombre de usuario y password
			$stmt = $conn->prepare("SELECT id, username, email, gender FROM users WHERE username = ? AND password = ?");
			$stmt->bind_param("ss",$username, $password);
			
			$stmt->execute();
			$stmt->store_result();
			
			//en caso de existir un registro anterior tomar sus valores y almacenarlos en un arreglo
			if($stmt->num_rows > 0){
				$stmt->bind_result($id, $username, $email, $gender);
				$stmt->fetch();
				
				//aqui el arreglo
				$user = array(
				'id'=>$id,
				'username'=>$username,
				'email'=>$email,
				'gender'=>$gender);
				
				
				//mensaje de confirmación de logeo exitoso
				$response['error'] = false;
				$response['message'] = 'Ingreso exitoso';
				$response['user'] = $user;
			}
			
			//en caso de no retornar un registro en base al username y password 
			else{
				$response['error'] = true;
				$response['message'] = 'nombre de usuario o contraseña incorrectas';
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
		
		//en caso de que no sea una operación válida
		default:
		$response['error'] = true;
		$response['message'] = 'Operación inválida';
	}	
}else{
	//en caso de que no se llame adecuadamente la operación apicall
	$response['error'] = true;
	$response['message'] = 'Operación API inválida';
}

//almacena el resultado del arreglo en formato JSON
echo json_encode($response);

//funcion que verifica que los parámetros que se pasan se encuentran definidos en la tabla de la BD
function isTheseParametersAvailable($params){
	foreach($params as $param){
		if(!isset($_POST[$param])){
			return false;
		}
	}
	return true;
}