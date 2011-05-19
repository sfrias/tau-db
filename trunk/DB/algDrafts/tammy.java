package Utils;

public class tammy {

}


joinedAtr = tablesArr[atr+1];
atrValRS = atrStmt.executeQuery(algorithmUtils.specificAttributeValuesQuery(joinedAtr, currentAtr, start_id));
unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
charactersWithAtr = algorithmUtils.getDirectConnectionString(joinedAtr, currentAtr, end_id);
while (atrValRS.next()){ //all attributes
	currentAtrVal = atrValRS.getInt(1);	
	if (currentAtrVal== unspecifiedId ) { //not relevant 
		continue;
		}
	if (!isEmpryQuery){
		charactersWithAtr += " OR ";
	}
	isEmpryQuery = false;
	charactersWithAtr +=  joinedAtr + "_" + currentAtr + "_id =" + currentAtrVal;
}
//closing all resources before executing a new query
if (atrValRS != null) atrValRS.close();
if (atrStmt != null) atrStmt.close();
atrValRS = null;
atrStmt = null;

if (!isEmpryQuery){
	charactersWithAtr += ")";
	charsWithAtrRS= charAtrStmt.executeQuery(charactersWithAtr);
	if (charsWithAtrRS.next()){ //change
		currentAtrVal =charsWithAtrRS.getInt(1);
	}
}
isEmpryQuery=true;
atr=atr+1;



joinedAtr = tablesArr[atr+1];
atrValRS = atrStmt.executeQuery(algorithmUtils.specificAttributeValuesQuery(joinedAtr, currentAtr, start_id));
unspecifiedId = unspecifiedIdOfTables.get(currentAtr);
charactersWithAtr =algorithmUtils.getAllValuesOfASpecificAttributes(joinedAtr, currentAtr, start_id, unspecifiedIdOfCharacter); //change
while (atrValRS.next()){ //all attributes
	currentAtrVal = atrValRS.getInt(1);
	if (currentAtrVal== unspecifiedId ) { //not relevant 
		continue;
		}
	if (!isEmpryQuery){
		charactersWithAtr += " OR ";
	}
	isEmpryQuery = false;
	charactersWithAtr +=  joinedAtr + "_" + currentAtr + "_id =" + currentAtrVal;
		
}
//closing resources of the previous query before executing a new query
if (atrValRS != null) atrValRS.close();
if (atrStmt != null) atrStmt.close();
atrStmt = null;
atrValRS = null;

if (!isEmpryQuery){ //
	charactersWithAtr += ")";
	charToAny = charWithAtrStmt.executeQuery(charactersWithAtr);
	foundMatch = helperForDirectConnectionToAll(charToAny, start_element, currentAtr,result);
	isEmpryQuery=true;
	}

atr = atr+1;
} 