databaseChangeLog = {

	changeSet(author: "casey (generated)", id: "1416702018541-1") {
		addColumn(tableName: "user") {
			column(name: "email", type: "varchar(255)") {
				constraints(nullable: "false", unique: "true")
			}
		}
	}

	changeSet(author: "casey (generated)", id: "1416702018541-2") {
		addColumn(tableName: "user") {
			column(name: "first_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "casey (generated)", id: "1416702018541-3") {
		addColumn(tableName: "user") {
			column(name: "last_name", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "casey (generated)", id: "1416702018541-4") {
		createIndex(indexName: "email_uniq_1416702018407", tableName: "user", unique: "true") {
			column(name: "email")
		}
	}
}
