<?php 
require_once 'DBconnection.php';
header ('Content-type: text/html; charset=utf-8'); 
$response = array();
 
if(isset($_GET['apicall']))
{
	switch($_GET['apicall'])
	{ 
		//Registrar un nuevo usuario
		case 'signup':
			if(isTheseParametersAvailable(array('username','email','password','gender')))
			{
				$username = $_POST['username']; 
				$email = $_POST['email']; 
				$password = md5($_POST['password']);
				$gender = $_POST['gender']; 
			 
				//Preparar la consulta
				$stmt = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
				$stmt->bind_param("ss", $username, $email);
				$stmt->execute();
				$stmt->store_result();
			 
				//si el resultado de la consulta contrae más de una fila, entonces ya existe un registro de ese usuario
				if($stmt->num_rows > 0)
				{
					$response['error'] = true;
					$response['message'] = 'El usuario ya esta registrado';
					$stmt->close();
			 
				//si no retorna resultado se prepara una consulta para ingresar al nuevo usuarip
				}
				else
				{
					//Preparar la consulta 
					$stmt = $conn->prepare("INSERT INTO users (username, email, password, gender) VALUES (?, ?, ?, ?)");
					$stmt->bind_param("ssss", $username, $email, $password, $gender);
				 
				//si se puede ejecutar la consulta se prepara una nueva consulta para adquirir el usuario registrado
					if($stmt->execute())
					{ 
						//preparar la consulta
						$stmt = $conn->prepare("SELECT id, id, username, email, gender FROM users WHERE username = ?"); 
						$stmt->bind_param("s",$username);
						$stmt->execute();
						$stmt->bind_result($userid, $id, $username, $email, $gender);
						$stmt->fetch();
					 
						//guardar el resultado de la consulta en un arreglo con su respectiva etiqueta
						$user = array(
						'id'=>$id, 
						'username'=>$username, 
						'email'=>$email,
						'gender'=>$gender
						);
					 
						//cerrar la conexion
						$stmt->close();
					 
						//retorno sin error de la ejecución del código
						$response['error'] = false; 
						$response['message'] = 'Usuario registrado con exito'; 
					 
						//la salida de la ejecución es el arreglo creado 
						$response['user'] = $user; 
					}
				}	
			//caso contrario, no se pudo extraer los datos de la tabla
			}
			else
			{
				$response['error'] = true; 
				$response['message'] = 'parametros no disponibles'; 
			}
		break; 

		case 'login': 
			if(isTheseParametersAvailable(array('username', 'password')))
			{
				$username = $_POST['username'];
				$password = md5($_POST['password']); 
				 
				$stmt = $conn->prepare("SELECT id, username, email, gender FROM users WHERE username = ? AND password = ?");
				$stmt->bind_param("ss",$username, $password);
				$stmt->execute();
				$stmt->store_result();
			 
				if($stmt->num_rows > 0)
				{ 
					$stmt->bind_result($id, $username, $email, $gender);
					$stmt->fetch();
			 
					$user = array(
					'id'=>$id, 
					'username'=>$username, 
					'email'=>$email,
					'gender'=>$gender
					);
			 
					$response['error'] = false; 
					$response['message'] = 'Login con exito'; 
					$response['user'] = $user; 
				}
				else
				{
					$response['error'] = false; 
					$response['message'] = 'usuario o contraseña incorrectas';
				}
			}
		break; 

		case 'newMeet':
			if(isTheseParametersAvailable(array('username','email','fecha','horario','motivo','nombre_institucion', 'num_personas')))
			{
				$username = $_POST['username'];
				$email = $_POST['email'];
				$fecha = $_POST['fecha'];
				$horario = $_POST['horario'];
				$motivo = $_POST['motivo'];
				$nombre_institucion = $_POST['nombre_institucion'];
				$num_personas = $_POST['num_personas'];
					
				$stmt = $conn->prepare("INSERT INTO reserva (username, email, fecha, horario, motivo, nombre_institucion, num_personas) 
										VALUES (?, ?, ?, ?, ?, ?, ?)"); 
				
				$stmt->bind_param("sssssss",$username, $email, $fecha, $horario, $motivo, $nombre_institucion, $num_personas);

				if($stmt->execute())
				{
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

					$response['error'] = false;
					$response['message'] = 'Se ha guardado su reserva';
					$response['meet'] = $meet;
				}
				
			}
			else
			{
				$response['error'] = true;
				$response['message'] = 'los argumentos no estan disponibles';
			}
		break;

		case 'getHours':		
				$stmt = $conn->prepare("SELECT hora FROM lista_horas");
				if($stmt->execute())
				{
					$myhours = array();
					while($row = $stmt->fetch_assoc())
					{
						$myhours['hora'][] = $row;
					}

					
					$response['error'] = false;
					$response['message'] = 'Tenga su lista de horas';
					$response['myhours'] = $myhours;
				}

		break;
		
		default: 
		$response['error'] = true; 
		$response['message'] = 'operacion invalida';
	}
 
}
else
{
	$response['error'] = true; 
	$response['message'] = 'llamada fallida al API';
}
 
echo json_encode($response);
 
function isTheseParametersAvailable($params)
{
	foreach($params as $param)
	{
		if(!isset($_POST[$param]))
		{
			return false; 
		}
	}
	return true; 
}
