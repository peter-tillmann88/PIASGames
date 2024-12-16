import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import Header from './AdminHeader';
import Footer from '../components/Footer';
import Modal from 'react-modal';


Modal.setAppElement('#root');

function ManageInventoryPage() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showAddProductForm, setShowAddProductForm] = useState(false);
    const [newProduct, setNewProduct] = useState({
        name: '',
        developer: '',
        description: '',
        price: '',
        stock: '',
        saleMod: '',
        platform: '',
        categoryIds: [],
    });
    const [selectedImages, setSelectedImages] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const [availableCategories, setAvailableCategories] = useState([]);
    const [successMessage, setSuccessMessage] = useState('');
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editProduct, setEditProduct] = useState(null);

    useEffect(() => {
        fetchProducts();
        fetchCategories();
    }, []);

    const fetchProducts = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/products/all');
            if (!response.ok) {
                throw new Error('Failed to fetch products');
            }
            const data = await response.json();
            setProducts(data);
            setErrorMessage('');
        } catch (error) {
            console.error('Error fetching products:', error);
            setProducts([]);
            setErrorMessage('Error fetching products: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchCategories = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/categories/all');
            if (!response.ok) {
                throw new Error('Failed to fetch categories');
            }
            const data = await response.json();
            setAvailableCategories(data);
        } catch (error) {
            console.error('Error fetching categories:', error);
            setAvailableCategories([
                { categoryId: 3, name: 'Shooter' },
                { categoryId: 4, name: 'Racing' },
                { categoryId: 23, name: 'Action' },
                { categoryId: 24, name: 'Comedy' },
                { categoryId: 26, name: 'RPG' },
                { categoryId: 27, name: 'Sports' },
                { categoryId: 28, name: 'Fighting' },
            ]);
        }
    };

    const handleImageChange = (e) => {
        const files = Array.from(e.target.files);
        const uniqueFiles = files.filter(file => 
            !selectedImages.some(selected => selected.name === file.name && selected.size === file.size)
        );
        if (selectedImages.length + uniqueFiles.length > 5) {
            setErrorMessage('You can only upload a maximum of 5 images.');
            return;
        }
        setSelectedImages((prevImages) => [...prevImages, ...uniqueFiles]);
    };

    const handleRemoveImage = (index) => {
        setSelectedImages((prevImages) => prevImages.filter((_, i) => i !== index));
    };

    const handleAddProduct = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');
        console.log('Submitting new product:', newProduct);
        try {
            if (newProduct.categoryIds.length === 0) {
                setErrorMessage('At least one valid Category is required.');
                return;
            }

            if (!newProduct.platform) {
                setErrorMessage('Platform selection is required.');
                return;
            }

            const categoryIds = newProduct.categoryIds.map(id => parseInt(id, 10)).filter(id => !isNaN(id));

            const formData = new FormData();

            const productData = JSON.stringify({
                name: newProduct.name,
                developer: newProduct.developer,
                description: newProduct.description,
                price: newProduct.price ? parseFloat(newProduct.price) : 0,
                stock: newProduct.stock ? parseInt(newProduct.stock, 10) : 0,
                saleMod: newProduct.saleMod ? parseFloat(newProduct.saleMod) : 0,
                platform: newProduct.platform,
                categoryIds: categoryIds,
                imageNames: [],
            });
            formData.append('product', new Blob([productData], { type: 'application/json' }));

            if (selectedImages && selectedImages.length > 0) {
                selectedImages.forEach((imgFile) => {
                    formData.append('images', imgFile);
                });
            }

            const response = await fetch('http://localhost:8080/api/products/add', {
                method: 'POST',
                body: formData,
            });

            if (response.ok) {
                console.log('Product added successfully');
                await fetchProducts();
                setShowAddProductForm(false);
                setNewProduct({
                    name: '',
                    developer: '',
                    description: '',
                    price: '',
                    stock: '',
                    saleMod: '',
                    platform: '',
                    categoryIds: [],
                });
                setSelectedImages([]);
                setSuccessMessage('Product added successfully!');
                setErrorMessage('');
            } else {
                const errorText = await response.text();
                console.error('Failed to add product:', errorText);
                setErrorMessage('Failed to add product: ' + errorText);
            }
        } catch (error) {
            console.error('Error adding product:', error);
            setErrorMessage('Error adding product: ' + error.message);
        }
    };

    const handleInputChange = (field, value) => {
        setNewProduct((prev) => ({ ...prev, [field]: value }));
    };

    const handleCategoryChange = (selectedOptions) => {
        const selectedIds = selectedOptions ? selectedOptions.map(option => option.value) : [];
        setNewProduct((prev) => ({ ...prev, categoryIds: selectedIds }));
    };

    const categoryOptions = availableCategories.length > 0 ? 
        availableCategories.map(category => ({
            value: category.categoryId.toString(),
            label: category.name,
        })) : [
            { value: '3', label: 'Shooter' },
            { value: '4', label: 'Racing' },
            { value: '23', label: 'Action' },
            { value: '24', label: 'Comedy' },
            { value: '26', label: 'RPG' },
            { value: '27', label: 'Sports' },
            { value: '28', label: 'Fighting' },
        ];

    //options
    const platformOptions = [
        { value: 'PS5', label: 'PS5' },
        { value: 'PS4', label: 'PS4' },
        { value: 'PC', label: 'PC' },
        { value: 'Nintendo Switch', label: 'Nintendo Switch' },
        { value: 'Xbox Series X', label: 'Xbox Series X' },
    ];

    //delete
    const handleDeleteProduct = async (productId) => {
        if (!window.confirm('Are you sure you want to delete this product?')) {
            return;
        }
        try {
            const response = await fetch(`http://localhost:8080/api/products/del/${productId}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                setSuccessMessage('Product deleted successfully!');
                setErrorMessage('');
                setProducts((prevProducts) => prevProducts.filter(product => product.productId !== productId));
            } else {
                const errorText = await response.text();
                console.error('Failed to delete product:', errorText);
                setErrorMessage('Failed to delete product: ' + errorText);
                setSuccessMessage('');
            }
        } catch (error) {
            console.error('Error deleting product:', error);
            setErrorMessage('Error deleting product: ' + error.message);
            setSuccessMessage('');
        }
    };
    const openEditModal = (product) => {
        setEditProduct({
            ...product,
            categoryIds: product.categoryList 
                ? Array.from(product.categoryList).map(cat => cat.categoryId.toString())
                : [],
        });
        setIsEditModalOpen(true);
        setErrorMessage('');
        setSuccessMessage('');
    };

    const closeEditModal = () => {
        setIsEditModalOpen(false);
        setEditProduct(null);
    };

    const handleEditInputChange = (field, value) => {
        setEditProduct((prev) => ({ ...prev, [field]: value }));
    };
    const handleEditCategoryChange = (selectedOptions) => {
        const selectedIds = selectedOptions ? selectedOptions.map(option => option.value) : [];
        setEditProduct((prev) => ({ ...prev, categoryIds: selectedIds }));
    };
    const handleUpdateProduct = async (e) => {
        e.preventDefault();
        setErrorMessage('');
        setSuccessMessage('');
        try {
            if (editProduct.categoryIds.length === 0) {
                setErrorMessage('At least one valid Category is required.');
                return;
            }

            if (!editProduct.platform) {
                setErrorMessage('Platform selection is required.');
                return;
            }
            const updatePayload = {
                name: editProduct.name,
                developer: editProduct.developer,
                description: editProduct.description,
                price: editProduct.price ? parseFloat(editProduct.price) : undefined,
                stock: editProduct.stock ? parseInt(editProduct.stock, 10) : undefined,
                saleMod: editProduct.saleMod ? parseFloat(editProduct.saleMod) : undefined,
                platform: editProduct.platform,
            };

            const response = await fetch(`http://localhost:8080/api/products/${editProduct.productId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatePayload),
            });

            if (response.ok) {
                const updatedProduct = await response.json();
                setSuccessMessage('Product updated successfully!');
                setErrorMessage('');
                setProducts((prevProducts) => prevProducts.map(product => 
                    product.productId === updatedProduct.productId ? updatedProduct : product
                ));
                closeEditModal();
            } else {
                const errorText = await response.text();
                console.error('Failed to update product:', errorText);
                setErrorMessage('Failed to update product: ' + errorText);
                setSuccessMessage('');
            }
        } catch (error) {
            console.error('Error updating product:', error);
            setErrorMessage('Error updating product: ' + error.message);
            setSuccessMessage('');
        }
    };

    return (
        <div className="flex flex-col min-h-screen">
            <Header />

            <main className="flex-grow p-6">
                <div className="container mx-auto">
                    <h1 className="text-4xl font-bold mb-6 text-center">Manage Inventory</h1>

                    <div className="mb-6">
                        <button
                            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700"
                            onClick={() => {
                                setShowAddProductForm(!showAddProductForm);
                                setErrorMessage('');
                                setSuccessMessage('');
                                if (showAddProductForm) {
                                    setNewProduct({
                                        name: '',
                                        developer: '',
                                        description: '',
                                        price: '',
                                        stock: '',
                                        saleMod: '',
                                        platform: '',
                                        categoryIds: [],
                                    });
                                    setSelectedImages([]);
                                }
                            }}
                        >
                            {showAddProductForm ? 'Cancel' : 'Add Product'}
                        </button>

                        {showAddProductForm && (
                            <form
                                onSubmit={handleAddProduct}
                                className="bg-gray-100 p-4 rounded mt-4 shadow"
                            >
                                {/* developer */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Developer:</label>
                                    <input
                                        type="text"
                                        value={newProduct.developer}
                                        onChange={(e) => handleInputChange('developer', e.target.value)}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        required
                                    />
                                </div>

                                {/* name */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Name:</label>
                                    <input
                                        type="text"
                                        value={newProduct.name}
                                        onChange={(e) => handleInputChange('name', e.target.value)}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        required
                                    />
                                </div>

                                {/* desc */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Description:</label>
                                    <textarea
                                        value={newProduct.description}
                                        onChange={(e) => handleInputChange('description', e.target.value)}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        required
                                    />
                                </div>

                                {/* price$ */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Price:</label>
                                    <input
                                        type="number"
                                        value={newProduct.price}
                                        onChange={(e) => handleInputChange('price', e.target.value)}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        required
                                        min="0"
                                        step="0.01"
                                    />
                                </div>

                                {/* quanitity */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Stock:</label>
                                    <input
                                        type="number"
                                        value={newProduct.stock}
                                        onChange={(e) => handleInputChange('stock', e.target.value)}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        required
                                        min="0"
                                    />
                                </div>

                                {/* sale mod (remove) */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Sale Modifier:</label>
                                    <input
                                        type="number"
                                        value={newProduct.saleMod}
                                        onChange={(e) => handleInputChange('saleMod', e.target.value)}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        step="0.01"
                                        min="0"
                                    />
                                </div>

                                {/* platofrm */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Platform:</label>
                                    <select
                                        value={newProduct.platform}
                                        onChange={(e) => handleInputChange('platform', e.target.value)}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        required
                                    >
                                        <option value="">Select Platform</option>
                                        {platformOptions.map((platform) => (
                                            <option key={platform.value} value={platform.value}>
                                                {platform.label}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                {/* cat selection */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Categories:</label>
                                    <Select
                                        isMulti
                                        name="categories"
                                        options={categoryOptions}
                                        className="basic-multi-select"
                                        classNamePrefix="select"
                                        value={categoryOptions.filter(option => newProduct.categoryIds.includes(option.value))}
                                        onChange={handleCategoryChange}
                                        placeholder="Select categories..."
                                        required
                                        menuPortalTarget={document.body}
                                        styles={{
                                            menuPortal: base => ({ ...base, zIndex: 9999 }),
                                            menu: provided => ({ ...provided, zIndex: 9999 }),
                                            control: provided => ({
                                                ...provided,
                                                borderColor: '#cbd5e0',
                                                '&:hover': {
                                                    borderColor: '#a0aec0',
                                                },
                                            }),
                                            option: (provided, state) => ({
                                                ...provided,
                                                backgroundColor: state.isSelected
                                                    ? '#3182ce'
                                                    : state.isFocused
                                                    ? '#bee3f8'
                                                    : 'white',
                                                color: state.isSelected ? 'white' : 'black',
                                                cursor: 'pointer',
                                            }),
                                        }}
                                    />
                                    <p className="text-sm text-gray-500 mt-1">
                                        Use Ctrl (Windows) or Command (Mac) to select multiple categories.
                                    </p>
                                </div>

                                {/* images */}
                                <div className="mb-4">
                                    <label className="block text-sm font-medium text-gray-700">Images (JPG File Only & First photo inputted is cover photo.):</label>
                                    <input
                                        type="file"
                                        multiple
                                        onChange={handleImageChange}
                                        className="w-full border border-gray-300 rounded px-3 py-2"
                                        accept="image/*"
                                    />
                                    {/* display images (first will be cover photo) */}
                                    {selectedImages.length > 0 && (
                                        <div className="mt-2 flex flex-wrap gap-2">
                                            {selectedImages.map((img, index) => (
                                                <div key={index} className="relative">
                                                    <img
                                                        src={URL.createObjectURL(img)}
                                                        alt={`Preview ${index}`}
                                                        className="w-20 h-20 object-cover rounded"
                                                    />
                                                    <button
                                                        type="button"
                                                        className="absolute top-0 right-0 bg-red-500 text-white rounded-full p-1"
                                                        onClick={() => handleRemoveImage(index)}
                                                    >
                                                        &times;
                                                    </button>
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </div>

                                <button
                                    type="submit"
                                    className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700"
                                >
                                    Add Product
                                </button>
                                {errorMessage && (
                                    <p className="text-red-500 mt-2">{errorMessage}</p>
                                )}
                                {successMessage && (
                                    <p className="text-green-500 mt-2">{successMessage}</p>
                                )}
                            </form>
                        )}
                    </div>

                    {errorMessage && !showAddProductForm && (
                        <p className="text-red-500 mb-4">{errorMessage}</p>
                    )}
                    {successMessage && !showAddProductForm && (
                        <p className="text-green-500 mb-4">{successMessage}</p>
                    )}

                    {loading ? (
                        <div>Loading products...</div>
                    ) : (
                        <table className="w-full border-collapse border border-gray-300">
                            <thead>
                                <tr className="bg-gray-200">
                                    <th className="border border-gray-300 px-4 py-2">Name</th>
                                    <th className="border border-gray-300 px-4 py-2">Developer</th>
                                    <th className="border border-gray-300 px-4 py-2">Price</th>
                                    <th className="border border-gray-300 px-4 py-2">Stock</th>
                                    <th className="border border-gray-300 px-4 py-2">Platform</th>
                                    <th className="border border-gray-300 px-4 py-2">SaleMod</th>
                                    <th className="border border-gray-300 px-4 py-2">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {products.map((product) => (
                                    <tr key={product.productId} className="text-center">
                                        <td className="border border-gray-300 px-4 py-2">{product.name}</td>
                                        <td className="border border-gray-300 px-4 py-2">{product.developer}</td>
                                        <td className="border border-gray-300 px-4 py-2">
                                            ${Number(product.price).toFixed(2)}
                                        </td>
                                        <td className="border border-gray-300 px-4 py-2">{product.stock}</td>
                                        <td className="border border-gray-300 px-4 py-2">{product.platform}</td>
                                        <td className="border border-gray-300 px-4 py-2">{product.saleMod}</td>
                                        <td className="border border-gray-300 px-4 py-2 space-x-2">
                                            <button
                                                className="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-700"
                                                onClick={() => openEditModal(product)}
                                            >
                                                Edit
                                            </button>
                                            <button
                                                className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700"
                                                onClick={() => handleDeleteProduct(product.productId)}
                                            >
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}

                    <Modal
                        isOpen={isEditModalOpen && editProduct !== null}
                        onRequestClose={closeEditModal}
                        contentLabel="Edit Product"
                        className="max-w-3xl mx-auto mt-20 bg-white p-6 rounded shadow-lg overflow-auto max-h-screen"
                        overlayClassName="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-start z-50"
                    >
                        {editProduct && (
                            <div>
                                <h2 className="text-2xl font-bold mb-4">Edit Product</h2>
                                <form onSubmit={handleUpdateProduct}>
                                    {/* developer*/}
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Developer:</label>
                                        <input
                                            type="text"
                                            value={editProduct.developer}
                                            onChange={(e) => handleEditInputChange('developer', e.target.value)}
                                            className="w-full border border-gray-300 rounded px-3 py-2"
                                            required
                                        />
                                    </div>

                                    {/* name */}
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Name:</label>
                                        <input
                                            type="text"
                                            value={editProduct.name}
                                            onChange={(e) => handleEditInputChange('name', e.target.value)}
                                            className="w-full border border-gray-300 rounded px-3 py-2"
                                            required
                                        />
                                    </div>

                                    {/* desc */}
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Description:</label>
                                        <textarea
                                            value={editProduct.description}
                                            onChange={(e) => handleEditInputChange('description', e.target.value)}
                                            className="w-full border border-gray-300 rounded px-3 py-2"
                                            required
                                        />
                                    </div>

                                    {/*price*/}
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Price:</label>
                                        <input
                                            type="number"
                                            value={editProduct.price}
                                            onChange={(e) => handleEditInputChange('price', e.target.value)}
                                            className="w-full border border-gray-300 rounded px-3 py-2"
                                            required
                                            min="0"
                                            step="0.01"
                                        />
                                    </div>

                                    {/* stock */}
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Stock:</label>
                                        <input
                                            type="number"
                                            value={editProduct.stock}
                                            onChange={(e) => handleEditInputChange('stock', e.target.value)}
                                            className="w-full border border-gray-300 rounded px-3 py-2"
                                            required
                                            min="0"
                                        />
                                    </div>

                                    {/* sale mod  */}
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Sale Modifier:</label>
                                        <input
                                            type="number"
                                            value={editProduct.saleMod}
                                            onChange={(e) => handleEditInputChange('saleMod', e.target.value)}
                                            className="w-full border border-gray-300 rounded px-3 py-2"
                                            step="0.01"
                                            min="0"
                                        />
                                    </div>

                                    {/* console*/}
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Platform:</label>
                                        <select
                                            value={editProduct.platform}
                                            onChange={(e) => handleEditInputChange('platform', e.target.value)}
                                            className="w-full border border-gray-300 rounded px-3 py-2"
                                            required
                                        >
                                            <option value="">Select Platform</option>
                                            {platformOptions.map((platform) => (
                                                <option key={platform.value} value={platform.value}>
                                                    {platform.label}
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    {/* cat seleciton
                                    <div className="mb-4">
                                        <label className="block text-sm font-medium text-gray-700">Categories:</label>
                                        <Select
                                            isMulti
                                            name="categories"
                                            options={categoryOptions}
                                            className="basic-multi-select"
                                            classNamePrefix="select"
                                            value={categoryOptions.filter(option => editProduct.categoryIds.includes(option.value))}
                                            onChange={handleEditCategoryChange}
                                            placeholder="Select categories..."
                                            required
                                            menuPortalTarget={document.body}
                                            styles={{
                                                menuPortal: base => ({ ...base, zIndex: 9999 }),
                                                menu: provided => ({ ...provided, zIndex: 9999 }),
                                                control: provided => ({
                                                    ...provided,
                                                    borderColor: '#cbd5e0',
                                                    '&:hover': {
                                                        borderColor: '#a0aec0',
                                                    },
                                                }),
                                                option: (provided, state) => ({
                                                    ...provided,
                                                    backgroundColor: state.isSelected
                                                        ? '#3182ce'
                                                        : state.isFocused
                                                        ? '#bee3f8'
                                                        : 'white',
                                                    color: state.isSelected ? 'white' : 'black',
                                                    cursor: 'pointer',
                                                }),
                                            }}
                                        />
                                        <p className="text-sm text-gray-500 mt-1">
                                            Use Ctrl (Windows) or Command (Mac) to select multiple categories.
                                        </p>
                                    </div> */}

                                    <button
                                        type="submit"
                                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700"
                                    >
                                        Update Product
                                    </button>
                                    <button
                                        type="button"
                                        onClick={closeEditModal}
                                        className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700 ml-2"
                                    >
                                        Cancel
                                    </button>
                                    {errorMessage && (
                                        <p className="text-red-500 mt-2">{errorMessage}</p>
                                    )}
                                    {successMessage && (
                                        <p className="text-green-500 mt-2">{successMessage}</p>
                                    )}
                                </form>
                            </div>
                        )}
                    </Modal>
                </div>
            </main>

            <Footer />
        </div>
    );

}

export default ManageInventoryPage;