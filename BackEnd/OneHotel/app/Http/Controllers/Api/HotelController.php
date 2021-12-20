<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Validator;
use App\Models\Hotel;

class HotelController extends Controller
{
    public function index()
    {
        $hotels = Hotel::all();

        if(count($hotels) > 0)
        {
            return response([
                'message' => 'Retrieve All Success',
                'hotel' => $hotels
            ], 200);
        }
        else
        {
            return response([
                'message' => 'Empty',
                'hotel' => null
            ], 400);
        }
    }

    public function show($id)
    {
        $hotel = Hotel::find($id);

        if(!is_null($hotel))
        {
            return response([
                'message' => 'Retrieve Hotel Success',
                'hotel' => $hotel
            ], 200);
        }
        else
        {
            return response([
                'message' => 'Hotel Not Found',
                'hotel' => null
            ], 404);
        }
    }

    public function store(Request $request)
    {
        $storeData = $request->all();
        $validate = Validator::make($storeData, [
            'nama' => 'required|max:60|unique:hotels',
            'rating' => 'required|numeric',
            'lokasi' => 'required',
            'urlfoto' => 'required'
        ]);

        if($validate->fails())
        {
            return response(['message' => $validate->errors()], 400);
        }

        $hotel = Hotel::create($storeData);
        return response([
            'message' => 'Add Hotel Success',
            'hotel' => $hotel
        ], 200);
    }

    public function destroy($id)
    {
        $hotel = Hotel::find($id);

        if(is_null($hotel))
        {
            return response([
                'message' => 'Hotel Not Found',
                'hotel' => null
            ], 404);
        }
        
        if($hotel->delete())
        {
            return response([
                'message' => 'Delete Hotel Success',
                'hotel' => $hotel
            ], 200);
        }

        return response([
            'message' => 'Delete Hotel Failed',
            'hotel' => null
        ], 400);
    }

    public function update(Request $request, $id)
    {
        $hotel = Hotel::find($id);

        if(is_null($hotel))
        {
            return response([
                'message' => 'Hotel Not Found',
                'hotel' => null
            ], 404);
        }

        $updateData = $request->all();
        $validate = Validator::make($updateData, [
          'nama' => 'required|max:60',
          'rating' => 'required|numeric',
          'lokasi' => 'required',
          'urlfoto' => 'required'
        ]);

        if($validate->fails())
        {
            return response(['message' => $validate->errors()], 400);
        }

        $hotel->nama = $updateData['nama'];
        $hotel->rating = $updateData['rating'];
        $hotel->lokasi = $updateData['lokasi'];
        $hotel->urlfoto = $updateData['urlfoto'];

        if($hotel->save())
        {
            return response([
                'message' => 'Update Hotel Success',
                'hotel' => $hotel
            ], 200);
        }

        return response([
            'message' => 'Update Hotel Failed',
            'hotel' => null
        ], 400);
    }
}
