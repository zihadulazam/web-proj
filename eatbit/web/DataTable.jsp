<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        
 	<meta charset="utf-8">
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

	<title>Eatbit | Search</title>
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- Bootstrap -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
    </head>
    <body>
        
       <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>

        <!-- Main Content -->
            <div class="container">
                <div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
                    <section>            
                        <table id="example" class="table table-striped table-bordered dt-responsive nowrap dataTable no-footer dtr-inline collapsed" cellspacing="0" width="100%" role="grid" aria-describedby="example_info" style="width: 100%;">
                            <thead>
                               <tr>
                                    <th>name</th>
                                    <th>description</th>
                                    <th>web_site_url</th>
                                </tr>
                            </thead>
                        </table> 
                    </section>
                </div>
          </div>
        <!-- end Main container -->  
        
                <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
        
        <!-- Datatable -->
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">
        
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/responsive/2.1.0/js/dataTables.responsive.min.js">
	</script>
        <script type="text/javascript" language="javascript" src="https://cdn.datatables.net/responsive/2.1.0/js/responsive.bootstrap.min.js">
	</script>
	
	<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
        
        <script type="text/javascript" language="javascript" class="init">
            $(document).ready(function() {
                    $('#example').DataTable( {
                            "processing": true,
                            "serverSide": true,
                            "ajax":"http://localhost:8080/eatbit/PopulateTable" ,
                            "columns":[{"data":"name"},{"data":"description"},{"data":"web_site_url"}]
                    } );
            } );
	</script>
    </body>
</html>
