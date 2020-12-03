import React from 'react';
import { Switch } from 'react-router-dom';
import PrivateRoute from 'core/components/Routes/PrivateRoute';
import NavBar from './components/NavBar';
import Products from './components/Products';
import './styles.scss';

const Admin = () => (
    <div className='admin-container'>
        <NavBar/>
        <div className="admin-content">
            <Switch>
                <PrivateRoute path='/admin/products'>
                    <Products/>
                </PrivateRoute>
                <PrivateRoute path='/admin/categories'>
                    <h1>Categorias</h1>
                </PrivateRoute>
                <PrivateRoute path='/admin/users' allowedRoutes={["ROLE_ADMIN"]}>
                    <h1>Usuários</h1>
                </PrivateRoute>
            </Switch>
        </div>
    </div>
);

export default Admin;