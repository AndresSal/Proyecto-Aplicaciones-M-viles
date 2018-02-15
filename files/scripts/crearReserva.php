<?php

require_once 'DBconnection.php';
header ('Content-type: text/html; charset=utf-8');

if(isTheseParametersAvailable(array('username','email','fecha','horario','motivo','nombre_institucion', 'num_personas')))
{
	$username = $_POST['username'];
	$email = $_POST['email'];
	$fecha = $_POST['fecha'];
	$horario = $_POST['horario'];
	$motivo = $_POST['motivo'];
	$nombre_institucion = $_POST['nombre_institucion'];
	$num_personas = $_POST['num_personas'];
	
	$query = "INSERT INTO reserva (username, email, fecha, horario, motivo, nombre_institucion, num_personas) 
										VALUES (?, ?, ?, ?, ?, ?, ?)";
										
	$result = mysqli_query($conn, $query);
	$query = "SELECT fecha FROM reserva WHERE username = ? AND horario = ?";
	$result = mysqli_query($conn, $query);
	
	$rawdata = array();

	while($row = mysqli_fetch_assoc($result))
	{
		$rawdata['fecha_reserva'][] = $row;
	}
	
	echo json_encode($rawdata, 128);
}
else
{
	$response = array();
	$response['error'] = true;
	$response['message'] = 'los argumentos no estan disponibles';
	echo json_encode($response, 128);
}
		
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
?>