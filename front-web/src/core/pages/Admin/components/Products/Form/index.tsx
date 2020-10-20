import { makeRequest } from 'core/utils/request';
import React, { useState } from 'react';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState = {
    name: string,
    price: string,
    category:string,
    description:string
}

const Form = () => {
    const [formData, setFormData] = useState<FormState>({
        name:"",
        price: '',
        category:"",
        description:''
    });

type FormEvent = React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>

    const handleOnChange = (event: FormEvent) => {
        const name = event.target.name;
        const value = event.target.value;
        setFormData(data => ({...data, [name]:value}));
    }
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) =>{
        event.preventDefault();
        const payload = {
            ...formData,
            imgUrl:'https://imagens.canaltech.com.br/ofertas/o14307.1.jpg',
            categories: [{ id: formData.category}]
        }
        makeRequest({url:'/products', method:'POST',data:payload})
        .then(() =>{
            setFormData({name:'', category:'', price:'', description:''})
        });
    }

    return (
        <form onSubmit={handleSubmit}>
            <BaseForm title="casdastrar um produto">
                <div className="row">
                    <div className="col-6">
                        <input value={formData?.name}
                            name='name'
                            type='text'
                            className='form-control mb-5'
                            onChange={handleOnChange}
                            placeholder='Nome Completo' />

                        <input value={formData?.price}
                            name='price'
                            type='text'
                            className='form-control mb-5'
                            onChange={handleOnChange}
                            placeholder="Preço" />

                        <select 
                        name='category'
                        value={formData?.category} 
                        className='form-control mb-5' 
                        onChange={handleOnChange}>
                            <option value="3">Computadores</option>
                            <option value="1">Livros</option>
                            <option value="2">Electronics</option>
                        </select>
                    </div>
                    <div className="col-6">
                        <textarea 
                        onChange={handleOnChange}
                        value={formData.description}
                        name='description'
                        className='form-control' 
                        cols={30} 
                        rows={10}/>
                    </div>
                </div>
            </BaseForm>
        </form>
    );
}
export default Form;