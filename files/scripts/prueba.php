<?php
	
	require_once 'DBconnection.php';
	header ('Content-type: text/html; charset=utf-8');
	
	//Establece la consulta a realizar para la base de datos con el id recuperado del mensaje
	$query = "SELECT hora FROM lista_horas";


	//Realiza la consula a la base de datos
	$result = mysqli_query($conn, $query);

	//Definición de un arreglo para almacenar los resultados de la consulta
	$rawdata = array();

	//Almacenar los elementos recuperados en el arreglo
	while($row = mysqli_fetch_assoc($result))
	{
		$rawdata['horas'][] = $row;
	}

	//Publicación de los resultados en formato JSON
	echo json_encode($rawdata, 128);

	
?>