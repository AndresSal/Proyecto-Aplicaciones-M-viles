<?php

require_once 'conexion.php';

$response = array();

if(isset($_GET['apicall'])){
	switch($_GET['apicall']){
		case 'signup':
		if(isTheseParametersAvailable(array('USERNAME','EMAIL','PASSWORD','GENDER'))){
			$username = $_POST['USERNAME'];
			$email = $_POST['EMAIL'];
			$password = $_POST['PASSWORD'];
			$gender = $_POST['GENDER'];
			
			//preparar la consulta, obtener id a partir del nombre de usuario o correo
			$stmt = $conn->prepare("SELECT ID FROM user WHERE USERNAME = ? OR EMAIL = ?");
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
				$stmt = $conn->prepare("INSERT INTO user (USERNAME, EMAIL, PASSWORD, GENDER) VALUES (?, ?, ?, ?)");
				$stmt->bind_param("ssss", $username, $email, $password, $gender);
				
				//si logra ejecutarse la consulta toma como resultados la respuesta y  los almacena en un arreglo
				if($stmt->execute()){
					$stmt = $conn->prepare("SELECT ID, ID, USERNAME, EMAIL, GENDER FROM user WHERE USERNAME = ?");
					$stmt->bind_param("s", $username);
					$stmt->execute();
					$stmt->bind_result($userid, $id, $username, $email, $gender);
					$stmt->fetch();
					
					//almacenados en un arreglo
					$user = array()(
						'ID' => $id;
						'USERNAME' => $username;
						'EMAIL' => $email;
						'GENDER' => $gender;
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
		if(isTheseParametersAvailable(array('USERNAME','PASSWORD'))){
			$username = $_POST['USERNAME'];
			$username = md5($_POST['PASSWORD']);
			
			//prepara la consulta tomando en cuenta solo el nombre de usuario y password
			$stmt = $conn->prepare("SELECT ID, USERNAME, EMAIL, GENDER FROM user WHERE USERNAME = ? AND PASSWORD = ?");
			$stmt->bind_param("ss",$username, $password);
			//se asume que la consulta se va a ejecutar sin problemas
			$stmt->execute();
			$stmt->store_result();
			//en caso de existir un registro anterior tomar sus valores y almacenarlos en un arreglo
			if($stmt->num_rows > 0){
				$stmt->bind_result($id, $username, $email, $gender);
				$stmt->fetch();
				//aqui el arreglo
				$user = array(
				'ID'=>$id,
				'USERNAME'=>$username,
				'ADDRESS'=>$email,
				'GENDER'=>$gender);
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
		if(isTheseParametersAvailable('USERNAME','HORARIO','FECHA','MOTIVO','NOMBRE_INSTITUCION','NUM_PERSONAS')){
			$username = $_POST['USERNAME'];
			$horario = $_POST['HOARIO'];
			$fecha = $_POST['FECHA'];
			$motivo = $_POST['MOTIVO'];
			$nombreIns = $_POST['NOMBRE_INSTITUCION'];
			$numPersonas = $_POST['NUM_PERSONAS'];
			
			$stmt = $conn->prepare
			("SELECT user.ID, IDRESERVA FROM reserva 
			INNER JOIN user ON reserva.ID=user.ID 
			WHERE USERNAME ?");
			
			$stmt->bind_param("s",$username);
			$stmt->execute();
			$stmt->store_result();
			
			if($stmt->num_rows > 0){
				$response['error']=true;
				$response['message']='reserva ya registrada';
				$stmt->close();
			}else{
				$stmt = $conn->prepare
			("INSERT INTO reserva(FECHA, HORARIO, MOTIVO, NOMBRE_INSTITUCION, NUM_PERSONAS) VALUES (?, ?, ?, ?, ?)"); 
				$stmt->bind_param("sssss",$fecha,$horario, $motivo, $nombreIns, $numPersonas);
				if($stmt->execute()){
					$stmt = $conn->prepare("SELECT ID, IDRESERVA, user.USERNAME, FECHA, HORARIO, MOTIVO, NOMBRE_INSTITUCION, NUM_PERSONAS 
					FROM reserva INNER JOIN user ON reserva.ID=user.ID
					WHERE user.USERNAME = ?");
					$stmt->bind_param("s",$username);
					$stmt->execute();
					$stmt->bind_result($userid, $idreserva, $username, $fecha, $horario, $motivo, $nombreIns, $numPersonas);
					$stmt->fetch();
					
					$meet = array(
						'ID' = $userid,
						'IDRESERVA' = $userreserva,
						'USERNAME' = $username,
						'FECHA' = $fecha,
						'HORARIO' = $motivo,
						'NOMBRE_INSTITUCION' = $nombreIns,
						'NUM_PERSONAS' = $numPersonas
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