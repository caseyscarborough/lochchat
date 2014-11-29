databaseChangeLog = {

	changeSet(author: "casey (generated)", id: "1417287608857-1") {
		addColumn(tableName: "notification") {
			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}
		}
	}
}
