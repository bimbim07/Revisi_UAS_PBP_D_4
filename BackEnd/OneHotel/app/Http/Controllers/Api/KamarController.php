<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Validator;
use App\Models\Kamar;

class KamarController extends Controller
{
    public function index()
    {
        $kamars = Kamar::all();

        if(count($kamars) > 0)
        {
            return response([
                'message' => 'Retrieve All Success',
                'kamar' => $kamars
            ], 200);
        }
        else
        {
            return response([
                'message' => 'Empty',
                'kamar' => null
            ], 400);
        }
    }

    public function show($id)
    {
        $kamar = Kamar::find($id);

        if(!is_null($kamar))
        {
            return response([
                'message' => 'Retrieve Kamar Success',
                'kamar' => $kamar
            ], 200);
        }
        else
        {
            return response([
                'message' => 'Kamar Not Found',
                'kamar' => null
            ], 404);
        }
    }

    public function store(Request $request)
    {
        $storeData = $request->all();
        $validate = Validator::make($storeData, [
            'id_hotel' => 'required',
            'nama' => 'required',
            'jenis' => 'required',
            'harga' => 'required|numeric',
            'deskripsi' => 'required',
            'urlfoto' => 'required'
        ]);

        if($validate->fails())
        {
            return response(['message' => $validate->errors()], 400);
        }

        $kamar = Kamar::create($storeData);
        return response([
            'message' => 'Add Kamar Success',
            'kamar' => $kamar
        ], 200);
    }

    public function destroy($id)
    {
        $kamar = Kamar::find($id);

        if(is_null($kamar))
        {
            return response([
                'message' => 'Kamar Not Found',
                'kamar' => null
            ], 404);
        }
        
        if($kamar->delete())
        {
            return response([
                'message' => 'Delete Kamar Success',
                'kamar' => $kamar
            ], 200);
        }

        return response([
            'message' => 'Delete Kamar Failed',
            'kamar' => null
        ], 400);
    }

    public function update(Request $request, $id)
    {
        $kamar = Kamar::find($id);

        if(is_null($kamar))
        {
            return response([
                'message' => 'Kamar Not Found',
                'kamar' => null
            ], 404);
        }

        $updateData = $request->all();
        $validate = Validator::make($updateData, [
          'nama' => 'required',
          'jenis' => 'required',
          'harga' => 'required|numeric',
          'deskripsi' => 'required',
          'urlfoto' => 'required'
        ]);

        if($validate->fails())
        {
            return response(['message' => $validate->errors()], 400);
        }

        $kamar->nama = $updateData['nama'];
        $kamar->jenis = $updateData['jenis'];
        $kamar->harga = $updateData['harga'];
        $kamar->deskripsi = $updateData['deskripsi'];
        $kamar->urlfoto = $updateData['urlfoto'];

        if($kamar->save())
        {
            return response([
                'message' => 'Update Kamar Success',
                'kamar' => $kamar
            ], 200);
        }

        return response([
            'message' => 'Update Kamar Failed',
            'kamar' => null
        ], 400);
    }
}
