package com.amanpatel.veggiestoretest0;

import android.util.Log;

import com.amanpatel.veggiestoretest0.Interface.ApiInterface;
import com.amanpatel.veggiestoretest0.Models.AddressBook;
import com.amanpatel.veggiestoretest0.Models.Cart;
import com.amanpatel.veggiestoretest0.Models.ProductCategory2;
import com.amanpatel.veggiestoretest0.Models.ProductPrice;
import com.amanpatel.veggiestoretest0.Models.Products;
import com.amanpatel.veggiestoretest0.Models.ProductsWithPrice;
import com.amanpatel.veggiestoretest0.Network.MyRetrofitInstance;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends ViewModel {
    private MutableLiveData<String> internetToast = new MutableLiveData<>();
    private MutableLiveData<String> operationToast = new MutableLiveData<>();

    private MutableLiveData<List<ProductCategory2>> category2 = new MutableLiveData<>();

    private MutableLiveData<List<ProductsWithPrice>> products1 = new MutableLiveData<>();
    private MutableLiveData<List<ProductsWithPrice>> searchProducts = new MutableLiveData<>();
    private MutableLiveData<ProductsWithPrice> product1 = new MutableLiveData<>();
    private MutableLiveData<List<Cart>> mCart = new MutableLiveData<>();
    private MutableLiveData<Cart> cCart = new MutableLiveData<>();
    private MutableLiveData<Cart> eCart = new MutableLiveData<>();
    private MutableLiveData<String> cartTotal = new MutableLiveData<>();
    private MutableLiveData<String> gCartTotal = new MutableLiveData<>();
    private MutableLiveData<Integer> deleteCode = new MutableLiveData<>();
    private MutableLiveData<ProductsWithPrice> cartProduct = new MutableLiveData<>();
    private MutableLiveData<List<AddressBook>> addresses = new MutableLiveData<>();


    public LiveData<String> getInternetToast() {
        return internetToast;
    }

    public LiveData<String> getOperationToast() {
        return operationToast;
    }

    public LiveData<List<ProductCategory2>> getCategory2(String cat1id) {
        loadCategory2(cat1id);
        return category2;
    }

    private void loadCategory2(String cat1Id) {
        final ApiInterface catRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<ProductCategory2>> call = catRetro.getProductCategory2(cat1Id);

        call.enqueue(new Callback<List<ProductCategory2>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductCategory2>> call, @NonNull Response<List<ProductCategory2>> response) {
                if(response.code() == 200 && response.body()!=null) {
                    if(response.body().size()!=0) {
                        category2.setValue(response.body());
                    }
                    else
                        operationToast.setValue("No data found.");
                }
                else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductCategory2>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot connect to server. Check your connection.");
            }
        });
    }

    public LiveData<List<ProductsWithPrice>> getProducts(String aid, String cat2) {
        loadProducts(aid, cat2);
        return products1;
    }

    private void loadProducts(String aid, String cat2) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<ProductsWithPrice>> call = productRetro.getProducts(aid, cat2);
        call.enqueue(new Callback<List<ProductsWithPrice>>() {
            @Override
            public void onResponse(Call<List<ProductsWithPrice>> call, Response<List<ProductsWithPrice>> response) {
                if(response.code() == 200 && response.body()!=null) {
                        products1.setValue(response.body());
                }
                else
                    operationToast.setValue("Server error.");
            }

            @Override
            public void onFailure(Call<List<ProductsWithPrice>> call, Throwable t) {
                internetToast.setValue("Cannot connect to server. Check your connection.");
            }
        });

    }

    public LiveData<List<ProductsWithPrice>> searchProducts(String searchString, String aid) {
        loadSearchData(searchString, aid);
        return searchProducts;
    }

    private void loadSearchData(String searchString, String aid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<ProductsWithPrice>> call = productRetro.searchProducts(searchString, aid);
        call.enqueue(new Callback<List<ProductsWithPrice>>() {
            @Override
            public void onResponse(Call<List<ProductsWithPrice>> call, Response<List<ProductsWithPrice>> response) {
                if (response.code() == 200)
                    searchProducts.setValue(response.body());
                else
                    internetToast.setValue("Server error.");
            }

            @Override
            public void onFailure(Call<List<ProductsWithPrice>> call, Throwable t) {
                internetToast.setValue("Cannot connect to server. Check your connection.");
            }
        });

    }

    public LiveData<Cart> addItemToCart(Cart cart) {
        addToCart(cart);
        return cCart;
    }

    private void addToCart(Cart cart) {
        final ApiInterface cartRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Cart> call = cartRetro.addToCart(cart);

        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                if (response.code() == 200) {
                    cCart.setValue(response.body());
                    operationToast.setValue("Item added.");
                } else {
                    operationToast.setValue("Operation failed.");
                    internetToast.setValue("Operation failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot connect to server. Check your connection.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    public LiveData<List<Cart>> getCart(String custid, String areaid) {
        loadCart(custid, areaid);
        return mCart;
    }

    private void loadCart(String custid, String areaid) {
        final ApiInterface cartRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Cart>> call = cartRetro.getCart(custid, areaid);

        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                if (response.code() == 200)
                    mCart.setValue(response.body());
                else
                    internetToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                internetToast.setValue("Failed to connect.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    public LiveData<Cart> editCartItem(Cart cart) {
        putCart(cart);
        return eCart;
    }

    private void putCart(Cart cart) {
        final ApiInterface cartRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Cart> call = cartRetro.editCartItem(cart.getId(), cart);

        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                if(response.code() == 200) {
                    eCart.setValue(response.body());
                    operationToast.setValue("Item updated.");
                }
                else {
                    operationToast.setValue("Operation failed.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                operationToast.setValue("Operation failed. Check connection.");
                Log.d("onFailure", t.toString());
            }
        });
    }

    public LiveData<ProductsWithPrice> getSingleProduct(String pid) {
        loadSingleProduct(pid);
        return cartProduct;
    }

    private void loadSingleProduct(final String pid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ProductsWithPrice> call = productRetro.getProduct(pid);

        call.enqueue(new Callback<ProductsWithPrice>() {
            @Override
            public void onResponse(Call<ProductsWithPrice> call, Response<ProductsWithPrice> response) {
                if(response.code() == 200) {
                    cartProduct.setValue(response.body());
                }
                else
                    internetToast.setValue("Server error.");
            }

            @Override
            public void onFailure(Call<ProductsWithPrice> call, Throwable throwable) {
                internetToast.setValue("Cannot connect.");
            }
        });
    }

    public LiveData<Integer> deleteCartItem(String id){
        deleteFromCart(id);
        return deleteCode;
    }

    private void deleteFromCart(String id) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Cart> call = productRetro.deleteCartItem(id);

        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(response.code() == 200) {
                    deleteCode.setValue(response.code());
                    operationToast.setValue("Item removed.");
                }
                else
                    operationToast.setValue("Operation failed.");
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable throwable) {
                operationToast.setValue("Operation failed. Check connection.");
            }
        });
    }

    public String addQuantity(String quantity) {
        Integer q = Integer.valueOf(quantity);
        q++;
        quantity = String.valueOf(q);
        return quantity;
    }

    public String subQuantity(String quantity) {
        Integer q = Integer.valueOf(quantity);
        if (q > 0)
            q--;
        quantity = String.valueOf(q);
        return quantity;
    }

    public LiveData<String> getCartTotal(String cusotmerid, String areaid) {
        getTotal(cusotmerid, areaid);
        return cartTotal;
    }

    private void getTotal(String cusotmerid, String areaid) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Double> call = productRetro.getCartTotal(cusotmerid, areaid);

        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if(response.code() == 200)
                    cartTotal.setValue(response.body().toString());
                else{
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable throwable) {
                internetToast.setValue("Cannot connect.");
            }
        });
    }

    public LiveData<String> getGCartTotal(List<Cart> cartList) {
        getGTotal(cartList);
        return gCartTotal;
    }

    private void getGTotal(List<Cart> cartList) {
        final ApiInterface productRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<Double> call = productRetro.getGCartTotal(cartList);

        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if(response.code() == 200)
                    gCartTotal.setValue(response.body().toString());
                else{
                    internetToast.setValue("Server error.");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable throwable) {
                internetToast.setValue("Cannot connect.");
            }
        });
    }

    public LiveData<List<AddressBook>> getAddresses(String cid, String areaname) {
        loadAddresses(cid, areaname);
        return addresses;
    }

    private void loadAddresses(String cid, String areaname) {
        final ApiInterface addressRetro = MyRetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<List<AddressBook>> call = addressRetro.getAddress(cid, areaname);

        call.enqueue(new Callback<List<AddressBook>>() {
            @Override
            public void onResponse(@NonNull Call<List<AddressBook>> call, @NonNull Response<List<AddressBook>> response) {
                if (response.code() == 200) {
                    addresses.setValue(response.body());
                } else
                    internetToast.setValue("Server error.");
            }

            @Override
            public void onFailure(@NonNull Call<List<AddressBook>> call, @NonNull Throwable t) {
                internetToast.setValue("Cannot connect. Check your connection.");
            }
        });
    }
}
