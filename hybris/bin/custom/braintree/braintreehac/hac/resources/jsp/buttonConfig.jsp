<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<html>
<head>
    <title>PayPal Button Config</title>

</head>
<body>
<div class="prepend-top span-17 colborder" id="content">
    <div class="marginLeft" id="inner">
        <h2>PayPal Button Config</h2>

        <table id="config">
            <thead>
            <tr>
                <th>Component</th>
                <th>Config</th>
            </tr>
            </thead>
            <tbody>
            <tr>
               <td width="25%">Cart Button Config</td>
                <td>
                    <form action="/hac/braintreehac/button/config/cart" method="GET">
                        <textarea rows="10" cols="45" name="cartButtonConfig" placeholder='${placeholder}' id="cartbuttontext"><c:out
                                value='${cartButtonConfig}'/></textarea>
                        <input id="cartbutton" type="submit" value="Save">
                    </form>
                </td>
            </tr>
            <tr>
                <td width="25%">
                    <p>MiniCart Button Config</p>
                    <p>("add to cart" and "mini cart")</p>
                </td>
                <td>
                    <form action="/hac/braintreehac/button/config/minicart" method="GET">
                        <textarea rows="10" cols="45" name="miniCartButtonConfig" placeholder='${placeholder}' id="minicartbuttontext"><c:out
                                value='${miniCartButtonConfig}'/></textarea>
                        <input id="minicartbutton" type="submit" value="Save">
                    </form>
                </td>
            </tr>
            <tr>
                <td width="25%">Billing Button Config</td>
                <td>
                    <form action="/hac/braintreehac/button/config/mark" method="GET">
                        <textarea rows="10" cols="45" name="markButtonConfig" placeholder='${placeholder}' id="markbuttontext"><c:out
                                value='${markButtonConfig}'/></textarea>
                        <input id="markbutton" type="submit" value="Save">
                    </form>
                </td>
            </tr>

            </tbody>
        </table>
    </div>
</div>
</body>
</html>

