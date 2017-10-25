<?php
require_once "config.php";

class DB {
	
	private $pdo;
	
	public function __construct() {
		$pdo = new PDO('mysql:host='.DB_HOST.';dbname='.DB_NAME.';charset=utf8', DB_USER, DB_PASS);
		$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$this->pdo = $pdo;
	}
	
	
	public function query($query, $params = array()) {
		$statement = $this->pdo->prepare($query);
		$statement->execute($params);
		if (explode(' ', $query)[0] == 'SELECT') {
			$data = $statement->fetchAll(PDO::FETCH_ASSOC);
			return $data;
		}
	}
	
	public function lastInsertId(){
		return $this->pdo->lastInsertId();
	}
	
}