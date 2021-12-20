<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateKamarsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('kamars', function (Blueprint $table) {
          $table->id();
          $table->unsignedBigInteger('id_hotel');
          $table->foreign('id_hotel')->references('id')->on('hotels');
          $table->string('nama');
          $table->string('jenis');
          $table->double('harga');
          $table->string('deskripsi');
          $table->longText('urlfoto')->nullable();
          $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('kamars');
    }
}
