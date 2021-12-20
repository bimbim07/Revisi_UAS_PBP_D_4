<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });

Route::post('register', 'Api\AuthController@register');
Route::post('login', 'Api\AuthController@login');
Route::get('user', 'Api\AuthController@index');

Route::group(['middleware' => 'auth:api'], function()
{
    Route::get('hotel', 'Api\HotelController@index');
    Route::get('hotel/{id}', 'Api\HotelController@show');
    Route::post('hotel', 'Api\HotelController@store');
    Route::put('hotel/{id}', 'Api\HotelController@update');
    Route::delete('hotel/{id}', 'Api\HotelController@destroy');

    Route::get('kamar', 'Api\KamarController@index');
    Route::get('kamar/{id}', 'Api\KamarController@show');
    Route::post('kamar', 'Api\KamarController@store');
    Route::put('kamar/{id}', 'Api\KamarController@update');
    Route::delete('kamar/{id}', 'Api\KamarController@destroy');

    Route::get('show/{id}', 'Api\AuthController@show');
    Route::put('update/{id}', 'Api\AuthController@update');
    Route::delete('delete/{id}', 'Api\AuthController@destroy');
});