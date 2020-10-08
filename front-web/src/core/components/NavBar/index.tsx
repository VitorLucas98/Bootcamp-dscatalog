import React from 'react';
import './styles.scss';

const Navbar = () => {
    return (
        <nav className='row bg-primary main-nav'>
            <div className='col-2'>
                <a href='teste' className='nav-log-text'>
                    <h4>DSCatalog</h4>
                </a>
            </div>
            <div className="col-6 offset-2">
                <ul className='main-menu'>
                    <li>
                        <a href='#home' className='active'> HOME</a>
                    </li>
                    <li>
                        <a href='#catagolo'>CATÁGOLO</a>
                    </li>
                    <li>
                        <a href='#admin'>ADMIN</a>
                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default Navbar;