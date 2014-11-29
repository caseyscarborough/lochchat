databaseChangeLog = {

	changeSet(author: "casey (generated)", id: "1417287049638-1") {
		addColumn(tableName: "notification") {
			column(name: "url", type: "varchar(1000)")
		}
	}
}
