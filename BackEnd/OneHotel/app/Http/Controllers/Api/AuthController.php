<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Models\User;
use Validator;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        $registrationData = $request->all();
        $validate = Validator::make($registrationData, [
            'email' => 'required|email:rfc,dns|unique:users',
            'fullname' => 'required',
            'username' => 'required',
            'password' => 'required'
        ]);

        if($validate->fails())
        {
            return response(['message' => $validate->errors()], 400);
        }

        $registrationData['password'] = bcrypt($request->password);
        $user = User::create($registrationData);
        return response([
            'message' => 'Register Success',
            'user' => $user
        ], 200);
    }

    public function login(Request $request)
    {
        $loginData = $request->all();
        $validate = Validator::make($loginData, [
          'email' => 'required|email:rfc,dns',
          'password' => 'required'
        ]);

        if($validate->fails())
        {
            return response(['message' => $validate->errors()], 400);
        }

        if(!Auth::attempt($loginData))
        {
            return response(['message' => 'Invalid Credentials'], 401); 
        }

        $user = Auth::user();
        $token = $user->createToken('Authentication Token')->accessToken;

        return response([
            'message' => 'Authenticated',
            'user' => $user,
            'token_type' => 'Bearer',
            'access_token' => $token
        ]);
    }

    public function index()
    {
        $users = User::all();

        if(count($users) > 0)
        {
            return response([
                'message' => 'Retrieve All Success',
                'user' => $users
            ], 200);
        }
        else
        {
            return response([
                'message' => 'Empty',
                'user' => null
            ], 400);
        }
    }

    public function show($id)
    {
        $user = User::find($id);

        if(!is_null($user))
        {
            return response([
                'message' => 'Retrieve User Success',
                'user' => $user
            ], 200);
        }
        else
        {
            return response([
                'message' => 'User Not Found',
                'user' => null
            ], 404);
        }
    }

    public function update(Request $request, $id)
    {
        $user = User::find($id);

        if(is_null($user))
        {
            return response([
                'message' => 'User Not Found',
                'user' => null
            ], 404);
        }

        $updateData = $request->all();
        $validate = Validator::make($updateData, [
          'fullname' => 'required',
          'username' => 'required',
        ]);

        if($validate->fails())
        {
            return response(['message' => $validate->errors()], 400);
        }

        $user->fullname = $updateData['fullname'];
        $user->username = $updateData['username'];
        $user->urlfoto = $updateData['urlfoto'];
        
        if($user->save())
        {
            return response([
                'message' => 'Update User Success',
                'user' => $user
            ], 200);
        }

        return response([
            'message' => 'Update User Failed',
            'user' => null
        ], 400);
    }

    public function destroy($id)
    {
        $user = User::find($id);

        if(is_null($user))
        {
            return response([
                'message' => 'User Not Found',
                'user' => null
            ], 404);
        }
        
        if($user->delete())
        {
            return response([
                'message' => 'Delete User Success',
                'user' => $user
            ], 200);
        }

        return response([
            'message' => 'Delete User Failed',
            'user' => null
        ], 400);
    }
}
