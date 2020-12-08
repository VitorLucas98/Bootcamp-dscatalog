import Pagination from 'core/components/Pagination';
import { ProductsResponse } from 'core/types/Product';
import { makePrivateRequest, makeRequest } from 'core/utils/request';
import React, { useCallback, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { toast } from 'react-toastify';
import Card from '../Card';
import CardLoader from '../Loaders/ProductCardLoader';

const List = () => {
    const [productResponse, setProductsResponse] = useState<ProductsResponse>();
    const [isLoading, setIsLoading] = useState(false);
    const [activePage, setActivePage] = useState(0);
    const history = useHistory();

    const getProducts = useCallback(() => {
        const params = {
            page: activePage,
            linesPerPage: 5,
            direction: 'DESC',
            orderBy: 'id'
        }
        setIsLoading(true);
        makeRequest({ url: '/products', params })
            .then(response => setProductsResponse(response.data))
            .finally(() => {
                setIsLoading(false)
            })
    }, [activePage]);

    useEffect(() => {
        getProducts()
    }, [getProducts]);

    const handleCreate = () => {
        history.push('/admin/products/create')
    }
    const onRemove = (productId: number) => {
        const confirm = window.confirm('Realmente deseja excluir esse produto ?')
        if (confirm) {
            makePrivateRequest({
                url: `/products/${productId}`,
                method: 'DELETE'
            })
                .then(() => {
                    toast.info('Produto deletado com sucesso!')
                    getProducts()
                }).catch(() => {
                    toast.error('Erro ao deletar produto!')
                })
        }
    }
    return (
        <div className='admin-products-list'>
            <button className='btn btn-primary btn-lg' onClick={handleCreate}>
                ADICIONAR
            </button>
            <div className="admin-list-container">
                {isLoading ? <CardLoader/> :
                ( productResponse?.content.map(product => (
                    <Card product={product} key={product.id} onRemove={onRemove} />
                )))}
                {productResponse &&
                    <Pagination
                        totalPages={productResponse.totalPages}
                        activePage={activePage}
                        onChange={page => setActivePage(page)}
                    />}
            </div>
        </div>
    );
}
export default List;
